package com.sofram.residente.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HabitacionRequest(

        @NotBlank
        @Size(max = 20)
        String numero,

        @NotNull
        @Min(1)
        Integer capacidad,

        @Size(max = 50)
        String tipo,

        @NotBlank
        @Size(max = 30)
        String estado
) {
}