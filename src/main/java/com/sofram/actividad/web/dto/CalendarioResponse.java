package com.sofram.actividad.web.dto;

public record CalendarioResponse(
        Long id,
        String nombre,
        String periodo,
        Integer anio,
        String estado
) {
}