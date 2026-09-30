package com.sofram.auth.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CambiarPasswordUsuarioRequest(

        @NotBlank
        String password
) {
}
