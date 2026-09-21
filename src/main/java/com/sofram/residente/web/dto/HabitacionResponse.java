package com.sofram.residente.web.dto;

public record HabitacionResponse(

        Long id,

        String numero,

        Integer capacidad,

        String tipo,

        String estado
) {
}