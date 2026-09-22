package com.sofram.historiaclinica.application;

import com.sofram.historiaclinica.domain.DetalleHistoriaClinica;
import com.sofram.historiaclinica.domain.HistoriaClinica;
import com.sofram.historiaclinica.infrastructure.persistence.DetalleHistoriaClinicaRepository;
import com.sofram.historiaclinica.infrastructure.persistence.HistoriaClinicaRepository;
import com.sofram.historiaclinica.web.dto.DetalleHistoriaClinicaRequest;
import com.sofram.historiaclinica.web.dto.DetalleHistoriaClinicaResponse;
import com.sofram.historiaclinica.web.dto.HistoriaClinicaRequest;
import com.sofram.historiaclinica.web.dto.HistoriaClinicaResponse;
import com.sofram.residente.application.ResidenteService;
import com.sofram.residente.domain.Residente;
import com.sofram.shared.exception.DuplicateResourceException;
import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoriaClinicaService {

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final DetalleHistoriaClinicaRepository detalleRepository;
    private final ResidenteService residenteService;

    public HistoriaClinicaService(
            HistoriaClinicaRepository historiaClinicaRepository,
            DetalleHistoriaClinicaRepository detalleRepository,
            ResidenteService residenteService
    ) {
        this.historiaClinicaRepository = historiaClinicaRepository;
        this.detalleRepository = detalleRepository;
        this.residenteService = residenteService;
    }

    @Transactional
    public HistoriaClinicaResponse crear(HistoriaClinicaRequest request) {

        if (historiaClinicaRepository.existsByResidenteId(request.residenteId())) {
            throw new DuplicateResourceException(
                    "El residente ya tiene una historia clínica"
            );
        }

        Residente residente =
                residenteService.buscarEntidadPorId(request.residenteId());

        HistoriaClinica historiaClinica = new HistoriaClinica();

        historiaClinica.setResidente(residente);
        historiaClinica.setFechaCreacion(request.fechaCreacion());
        historiaClinica.setObservaciones(request.observaciones());

        HistoriaClinica guardada =
                historiaClinicaRepository.save(historiaClinica);

        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public HistoriaClinicaResponse buscarPorId(Long id) {

        HistoriaClinica historiaClinica =
                historiaClinicaRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Historia clínica no encontrada con id: " + id
                                )
                        );

        return toResponse(historiaClinica);
    }

    @Transactional(readOnly = true)
    public HistoriaClinicaResponse buscarPorResidente(Long residenteId) {

        HistoriaClinica historiaClinica =
                historiaClinicaRepository.findByResidenteId(residenteId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "El residente no tiene historia clínica"
                                )
                        );

        return toResponse(historiaClinica);
    }

    @Transactional
    public DetalleHistoriaClinicaResponse agregarDetalle(
            Long historiaClinicaId,
            DetalleHistoriaClinicaRequest request
    ) {

        HistoriaClinica historiaClinica =
                historiaClinicaRepository.findById(historiaClinicaId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Historia clínica no encontrada con id: "
                                                + historiaClinicaId
                                )
                        );

        DetalleHistoriaClinica detalle =
                new DetalleHistoriaClinica();

        detalle.setHistoriaClinica(historiaClinica);
        detalle.setFecha(request.fecha());
        detalle.setObservaciones(request.observaciones());

        DetalleHistoriaClinica guardado =
                detalleRepository.save(detalle);

        return toDetalleResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<DetalleHistoriaClinicaResponse> listarDetalles(
            Long historiaClinicaId
    ) {

        if (!historiaClinicaRepository.existsById(historiaClinicaId)) {
            throw new ResourceNotFoundException(
                    "Historia clínica no encontrada con id: "
                            + historiaClinicaId
            );
        }

        return detalleRepository
                .findByHistoriaClinicaIdOrderByFechaDesc(historiaClinicaId)
                .stream()
                .map(this::toDetalleResponse)
                .toList();
    }

    private HistoriaClinicaResponse toResponse(
            HistoriaClinica historiaClinica
    ) {
        return new HistoriaClinicaResponse(
                historiaClinica.getId(),
                historiaClinica.getResidente().getId(),
                historiaClinica.getFechaCreacion(),
                historiaClinica.getObservaciones()
        );
    }

    private DetalleHistoriaClinicaResponse toDetalleResponse(
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