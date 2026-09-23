package com.sofram.actividad.web.dto;

import jakarta.validation.constraints.*;

public record ActividadRequest(

        @NotNull
        Long detalleCalendarioId,

        @NotNull
        Long empleadoId,

        @NotBlank
        @Size(max = 150)
        String nombre,

        @Size(max = 2000)
        String descripcion,

        @NotBlank
        @Size(max = 100)
        String tipo,

        @NotNull
        @Positive
        Integer duracion,

        @NotNull
        @Positive
        Integer cupoMaximo,

        @NotBlank
        @Size(max = 50)
        String estado

) {
}