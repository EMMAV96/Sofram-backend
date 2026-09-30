package com.sofram.historiaclinica.application;

import com.sofram.historiaclinica.domain.DetalleHistoriaClinica;
import com.sofram.historiaclinica.domain.HistoriaClinica;
import com.sofram.historiaclinica.web.dto.DetalleHistoriaClinicaResponse;
import com.sofram.historiaclinica.web.dto.HistoriaClinicaResponse;
import com.sofram.personal.domain.Empleado;
import org.springframework.stereotype.Component;

@Component
public class HistoriaClinicaMapper {

    public HistoriaClinicaResponse toResponse(
            HistoriaClinica historiaClinica
    ) {
        return new HistoriaClinicaResponse(
                historiaClinica.getId(),
                historiaClinica.getResidente().getId(),
                historiaClinica.getFechaCreacion(),
                historiaClinica.getObservaciones(),
                historiaClinica.getAntecedentesPersonales(),
                historiaClinica.getAntecedentesFamiliares(),
                historiaClinica.getAlergias()
        );
    }

    public DetalleHistoriaClinicaResponse toDetalleResponse(
            DetalleHistoriaClinica detalle
    ) {
        Empleado profesional = detalle.getProfesional();

        return new DetalleHistoriaClinicaResponse(
                detalle.getId(),
                detalle.getHistoriaClinica().getId(),
                detalle.getFecha(),
                detalle.getObservaciones(),
                profesional != null ? profesional.getId() : null,
                profesional != null ? profesional.getNombre() : null,
                profesional != null ? profesional.getApellido() : null,
                profesional != null ? profesional.getCargo().getNombre() : null
        );
    }
}
