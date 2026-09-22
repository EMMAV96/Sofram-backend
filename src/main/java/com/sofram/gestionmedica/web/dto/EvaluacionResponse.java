package com.sofram.gestionmedica.web.dto;

public record EvaluacionResponse(

        Long id,
        Long atencionMedicaId,
        Long detalleHistoriaClinicaId,
        String tipoEvaluacion,
        String descripcion,
        String planIntervencion
) {
}