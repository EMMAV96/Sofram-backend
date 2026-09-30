package com.sofram.auth.web.dto;

public record UsuarioResponse(

        Long id,
        String username,
        String rol,
        Long empleadoId,
        String empleadoNombre,
        String empleadoApellido,
        boolean activo
) {
}
