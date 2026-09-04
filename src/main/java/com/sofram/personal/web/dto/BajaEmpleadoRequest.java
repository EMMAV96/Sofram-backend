package com.sofram.personal.web.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BajaEmpleadoRequest(
        @NotNull
        LocalDate fechaBaja
) {
}
