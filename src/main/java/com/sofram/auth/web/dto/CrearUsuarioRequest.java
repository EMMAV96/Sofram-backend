package com.sofram.auth.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearUsuarioRequest(

        @NotNull
        Long empleadoId,

        @NotBlank
        @Size(max = 50)
        String username,

        @NotBlank
        String password,

        @NotBlank
        @Size(max = 50)
        String rol
) {
}
