package com.sofram.personal.web.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AsignacionTurnoResponse(
        Long id,
        Long empleadoId,
        Long turnoId,
        String turnoDescripcion,
        LocalTime horaInicio,
        LocalTime horaFin,
        LocalDate fechaDesde,
        LocalDate fechaHasta,
        String motivoCambio
) {
}
