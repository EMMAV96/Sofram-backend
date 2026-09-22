package com.sofram.gestionmedica.web.dto;

public record MedicacionResponse(

        Long id,
        Long detalleHistoriaClinicaId,
        String nombre,
        String dosis,
        String frecuencia

) {
}