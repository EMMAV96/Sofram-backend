package com.sofram.auth.application;

import com.sofram.auth.domain.Usuario;
import com.sofram.auth.infrastructure.persistence.UsuarioRepository;
import com.sofram.auth.infrastructure.security.JwtService;
import com.sofram.auth.web.dto.AuthResponse;
import com.sofram.auth.web.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
        } catch (DisabledException exception) {
            throw exception;
        } catch (AuthenticationException exception) {
            throw new BadCredentialsException("Credenciales invalidas");
        }

        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Credenciales invalidas"));
        usuario.registrarAcceso(LocalDateTime.now());

        String token = jwtService.generateToken(usuario);
        return new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                usuario.getUsername(),
                usuario.getRol().getNombre()
        );
    }
}
