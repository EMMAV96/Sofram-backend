package com.sofram.gestionmedica.application;

import com.sofram.gestionmedica.domain.*;
import com.sofram.gestionmedica.web.dto.*;
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

    public DiagnosticoResponse toDiagnosticoResponse(Diagnostico diagnostico) {
        return new DiagnosticoResponse(
                diagnostico.getId(),
                diagnostico.getDetalleHistoriaClinica().getId(),
                diagnostico.getDescripcion()
        );
    }

    public TratamientoResponse toTratamientoResponse(Tratamiento tratamiento) {
        return new TratamientoResponse(
                tratamiento.getId(),
                tratamiento.getDetalleHistoriaClinica().getId(),
                tratamiento.getNombre(),
                tratamiento.getDescripcion()
        );
    }

    public MedicacionResponse toMedicacionResponse(Medicacion medicacion) {
        return new MedicacionResponse(
                medicacion.getId(),
                medicacion.getDetalleHistoriaClinica().getId(),
                medicacion.getNombre(),
                medicacion.getDosis(),
                medicacion.getFrecuencia()
        );
    }

}