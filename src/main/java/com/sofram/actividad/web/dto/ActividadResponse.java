package com.sofram.actividad.web.dto;

public record ActividadResponse(

        Long id,
        Long detalleCalendarioId,
        Long empleadoId,
        String nombre,
        String descripcion,
        String tipo,
        Integer duracion,
        Integer cupoMaximo,
        String estado

) {
}