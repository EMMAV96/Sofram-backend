package com.sofram.historiaclinica.web.dto;

import java.time.LocalDate;

public record DetalleHistoriaClinicaResponse(

        Long id,
        Long historiaClinicaId,
        LocalDate fecha,
        String observaciones
) {
}