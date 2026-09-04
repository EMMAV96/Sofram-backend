package com.sofram.auth.web.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        String username,
        String rol
) {
}
