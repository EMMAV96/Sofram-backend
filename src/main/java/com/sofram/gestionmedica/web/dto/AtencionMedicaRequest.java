package com.sofram.gestionmedica.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AtencionMedicaRequest(

        @NotNull
        Long residenteId,

        @NotNull
        Long empleadoId,

        @NotNull
        LocalDate fecha,

        @Size(max = 500)
        String motivo,

        @Size(max = 100)
        String tipoIntervencion,

        @Size(max = 1000)
        String observaciones
) {
}