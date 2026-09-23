package com.sofram.actividad.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record DetalleCalendarioRequest(

        @NotNull
        LocalDate fecha,

        @NotNull
        LocalTime horaInicio,

        @NotNull
        LocalTime horaFin,

        @NotBlank
        @Size(max = 50)
        String estado

) {
}