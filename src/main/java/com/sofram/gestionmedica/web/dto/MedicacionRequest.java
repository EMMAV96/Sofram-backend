package com.sofram.gestionmedica.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MedicacionRequest(

        @NotNull
        Long detalleHistoriaClinicaId,

        @NotBlank
        @Size(max = 150)
        String nombre,

        @NotBlank
        @Size(max = 100)
        String dosis,

        @NotBlank
        @Size(max = 150)
        String frecuencia

) {
}