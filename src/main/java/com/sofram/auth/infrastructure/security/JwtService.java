package com.sofram.auth.infrastructure.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofram.auth.domain.Usuario;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JwtService {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder BASE64_URL_DECODER = Base64.getUrlDecoder();

    private final JwtProperties properties;
    private final ObjectMapper objectMapper;

    public JwtService(JwtProperties properties, ObjectMapper objectMapper) {
        if (properties.secret() == null || properties.secret().getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT_SECRET debe tener al menos 32 bytes");
        }
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public String generateToken(Usuario usuario) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(getExpirationSeconds());

        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", usuario.getUsername());
        payload.put("uid", usuario.getId());
        payload.put("rol", usuario.getRol().getNombre());
        payload.put("iat", issuedAt.getEpochSecond());
        payload.put("exp", expiresAt.getEpochSecond());

        String encodedHeader = encodeJson(header);
        String encodedPayload = encodeJson(payload);
        String unsignedToken = encodedHeader + "." + encodedPayload;
        return unsignedToken + "." + sign(unsignedToken);
    }

    public boolean isTokenValid(String token, String expectedUsername) {
        try {
            Map<String, Object> claims = parseAndValidate(token);
            return expectedUsername.equals(claims.get("sub"));
        } catch (JwtValidationException exception) {
            return false;
        }
    }

    public String extractUsername(String token) {
        Object subject = parseAndValidate(token).get("sub");
        if (!(subject instanceof String username) || username.isBlank()) {
            throw new JwtValidationException("Token sin sujeto valido");
        }
        return username;
    }

    public long getExpirationSeconds() {
        return properties.expirationMinutes() * 60;
    }

    private Map<String, Object> parseAndValidate(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new JwtValidationException("Formato de token invalido");
        }

        String unsignedToken = parts[0] + "." + parts[1];
        String expectedSignature = sign(unsignedToken);
        if (!MessageDigest.isEqual(
                expectedSignature.getBytes(StandardCharsets.UTF_8),
                parts[2].getBytes(StandardCharsets.UTF_8)
        )) {
            throw new JwtValidationException("Firma de token invalida");
        }

        Map<String, Object> claims = decodePayload(parts[1]);
        long expiresAt = getLongClaim(claims, "exp");
        if (Instant.now().getEpochSecond() >= expiresAt) {
            throw new JwtValidationException("Token expirado");
        }
        return claims;
    }

    private long getLongClaim(Map<String, Object> claims, String claimName) {
        Object value = claims.get(claimName);
        if (value instanceof Number number) {
            return number.longValue();
        }
        throw new JwtValidationException("Claim invalido: " + claimName);
    }

    private String encodeJson(Map<String, Object> value) {
        try {
            return BASE64_URL_ENCODER.encodeToString(objectMapper.writeValueAsBytes(value));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("No se pudo generar JWT", exception);
        }
    }

    private Map<String, Object> decodePayload(String encodedPayload) {
        try {
            byte[] payload = BASE64_URL_DECODER.decode(encodedPayload);
            return objectMapper.readValue(payload, new TypeReference<>() {
            });
        } catch (IllegalArgumentException | IOException exception) {
            throw new JwtValidationException("Payload de token invalido", exception);
        }
    }

    private String sign(String unsignedToken) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(properties.secret().getBytes(StandardCharsets.UTF_8), HMAC_SHA256));
            return BASE64_URL_ENCODER.encodeToString(mac.doFinal(unsignedToken.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo firmar JWT", exception);
        }
    }
}
