package com.sofram.actividad.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AsistenciaActividadRequest(

        @NotNull
        Boolean asistencia,

        @NotBlank
        @Size(max = 50)
        String estado,

        @Size(max = 1000)
        String observaciones

) {
}