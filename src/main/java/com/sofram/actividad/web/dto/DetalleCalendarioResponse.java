package com.sofram.actividad.web.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record DetalleCalendarioResponse(
        Long id,
        Long calendarioId,
        LocalDate fecha,
        LocalTime horaInicio,
        LocalTime horaFin,
        String estado
) {
}