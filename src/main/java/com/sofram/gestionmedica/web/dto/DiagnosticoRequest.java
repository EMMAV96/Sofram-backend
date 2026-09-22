package com.sofram.gestionmedica.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DiagnosticoRequest(

        @NotNull
        Long detalleHistoriaClinicaId,

        @NotBlank
        @Size(max = 2000)
        String descripcion

) {
}