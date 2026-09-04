package com.sofram.auth.web.dto;

public record CurrentUserResponse(
        Long id,
        String username,
        String rol
) {
}
