package com.sofram.gestionmedica.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EvaluacionRequest(

        @NotNull
        Long atencionMedicaId,

        @NotNull
        Long detalleHistoriaClinicaId,

        @NotBlank
        @Size(max = 100)
        String tipoEvaluacion,

        @NotBlank
        @Size(max = 2000)
        String descripcion,

        @Size(max = 2000)
        String planIntervencion
) {
}