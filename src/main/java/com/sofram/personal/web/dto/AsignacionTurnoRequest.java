package com.sofram.personal.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AsignacionTurnoRequest(
        @NotNull
        Long turnoId,

        @NotNull
        LocalDate fechaDesde,

        @Size(max = 255)
        String motivoCambio
) {
}
