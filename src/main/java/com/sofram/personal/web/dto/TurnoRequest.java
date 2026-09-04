package com.sofram.personal.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record TurnoRequest(
        @NotBlank
        @Size(max = 50)
        String descripcion,

        @NotNull
        LocalTime horaInicio,

        @NotNull
        LocalTime horaFin
) {
}
