package com.sofram.historiaclinica.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DetalleHistoriaClinicaRequest(

        @NotNull
        LocalDate fecha,

        @Size(max = 1000)
        String observaciones
) {
}