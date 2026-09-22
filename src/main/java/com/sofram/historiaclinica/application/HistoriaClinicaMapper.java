package com.sofram.historiaclinica.application;

import com.sofram.historiaclinica.domain.DetalleHistoriaClinica;
import com.sofram.historiaclinica.domain.HistoriaClinica;
import com.sofram.historiaclinica.web.dto.DetalleHistoriaClinicaResponse;
import com.sofram.historiaclinica.web.dto.HistoriaClinicaResponse;
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
                historiaClinica.getObservaciones()
        );
    }

    public DetalleHistoriaClinicaResponse toDetalleResponse(
            DetalleHistoriaClinica detalle
    ) {
        return new DetalleHistoriaClinicaResponse(
                detalle.getId(),
                detalle.getHistoriaClinica().getId(),
                detalle.getFecha(),
                detalle.getObservaciones()
        );
    }
}