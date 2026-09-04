package com.sofram.personal.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CargoRequest(
        @NotBlank
        @Size(max = 100)
        String nombre,

        @Size(max = 100)
        String sector,

        @Size(max = 50)
        String matricula,

        @Size(max = 100)
        String especialidad
) {
}
