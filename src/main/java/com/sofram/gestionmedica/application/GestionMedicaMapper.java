package com.sofram.gestionmedica.application;

import com.sofram.gestionmedica.domain.AtencionMedica;
import com.sofram.gestionmedica.domain.Evaluacion;
import com.sofram.gestionmedica.web.dto.AtencionMedicaResponse;
import com.sofram.gestionmedica.web.dto.EvaluacionResponse;
import org.springframework.stereotype.Component;

@Component
public class GestionMedicaMapper {

    public AtencionMedicaResponse toAtencionResponse(AtencionMedica atencion) {
        return new AtencionMedicaResponse(
                atencion.getId(),
                atencion.getResidente().getId(),
                atencion.getEmpleado().getId(),
                atencion.getFecha(),
                atencion.getMotivo(),
                atencion.getTipoIntervencion(),
                atencion.getObservaciones()
        );
    }

    public EvaluacionResponse toEvaluacionResponse(Evaluacion evaluacion) {
        return new EvaluacionResponse(
                evaluacion.getId(),
                evaluacion.getAtencionMedica().getId(),
                evaluacion.getDetalleHistoriaClinica().getId(),
                evaluacion.getTipoEvaluacion(),
                evaluacion.getDescripcion(),
                evaluacion.getPlanIntervencion()
        );
    }
}