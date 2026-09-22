package com.sofram.historiaclinica.web.dto;

import java.time.LocalDate;

public record HistoriaClinicaResponse(

        Long id,
        Long residenteId,
        LocalDate fechaCreacion,
        String observaciones
) {
}