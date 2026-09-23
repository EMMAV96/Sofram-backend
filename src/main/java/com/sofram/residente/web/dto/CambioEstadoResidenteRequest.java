package com.sofram.residente.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CambioEstadoResidenteRequest(

        @NotNull
        Long estadoId,

        @NotNull
        LocalDate fechaCambio,

        @Size(max = 255)
        String observacion

) {
}