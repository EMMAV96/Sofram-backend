package com.sofram.gestionmedica.web.dto;

import java.time.LocalDate;

public record AtencionMedicaResponse(

        Long id,
        Long residenteId,
        Long empleadoId,
        LocalDate fecha,
        String motivo,
        String tipoIntervencion,
        String observaciones
) {
}