package com.sofram.personal.web.dto;

import java.time.LocalTime;

public record TurnoResponse(
        Long id,
        String descripcion,
        LocalTime horaInicio,
        LocalTime horaFin
) {
}
