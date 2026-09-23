package com.sofram.actividad.web.dto;

import jakarta.validation.constraints.*;

public record CalendarioRequest(

        @NotBlank
        @Size(max = 150)
        String nombre,

        @NotBlank
        @Size(max = 100)
        String periodo,

        @NotNull
        @Min(2000)
        Integer anio,

        @NotBlank
        @Size(max = 50)
        String estado

) {
}