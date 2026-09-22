package com.sofram.gestionmedica.web.dto;


public record DiagnosticoResponse(

        Long id,
        Long detalleHistoriaClinicaId,
        String descripcion

) {
}