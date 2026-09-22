package com.sofram.gestionmedica.web.dto;

public record TratamientoResponse(

        Long id,
        Long detalleHistoriaClinicaId,
        String nombre,
        String descripcion

) {
}