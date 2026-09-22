package com.sofram.historiaclinica.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record HistoriaClinicaRequest(

        @NotNull
        Long residenteId,

        @NotNull
        LocalDate fechaCreacion,

        @Size(max = 1000)
        String observaciones
) {
}