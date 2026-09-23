package com.sofram.actividad.web.dto;

import java.time.LocalDate;

public record ParticipacionActividadResponse(

        Long id,
        Long actividadId,
        Long residenteId,
        LocalDate fecha,
        Boolean asistencia,
        String estado,
        String observaciones

) {
}