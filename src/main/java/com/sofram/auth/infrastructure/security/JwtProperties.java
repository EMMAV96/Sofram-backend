package com.sofram.auth.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sofram.security.jwt")
public record JwtProperties(String secret, long expirationMinutes) {
}
