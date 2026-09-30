package com.sofram.residente.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EgresoResidenteRequest(

        @NotNull
        LocalDate fechaEgreso,

        @NotNull
        Long estadoId,

        @Size(max = 255)
        String observacion

) {
}
