package com.sofram.actividad.application;

import com.sofram.actividad.domain.Actividad;
import com.sofram.actividad.domain.ParticipacionActividad;
import com.sofram.actividad.infrastructure.persistence.ParticipacionActividadRepository;
import com.sofram.actividad.web.dto.*;
import com.sofram.residente.application.ResidenteService;
import com.sofram.residente.domain.Residente;
import com.sofram.shared.exception.DuplicateResourceException;
import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipacionActividadService {

    private final ParticipacionActividadRepository repository;
    private final ActividadService actividadService;
    private final ResidenteService residenteService;

    public ParticipacionActividadService(
            ParticipacionActividadRepository repository,
            ActividadService actividadService,
            ResidenteService residenteService
    ) {
        this.repository = repository;
        this.actividadService = actividadService;
        this.residenteService = residenteService;
    }

    @Transactional
    public ParticipacionActividadResponse registrar(
            ParticipacionActividadRequest request
    ) {

        if (repository.existsByActividadIdAndResidenteId(
                request.actividadId(),
                request.residenteId()
        )) {
            throw new DuplicateResourceException(
                    "El residente ya está registrado en esta actividad"
            );
        }

        Actividad actividad =
                actividadService.buscarEntidadPorId(
                        request.actividadId()
                );

        Residente residente =
                residenteService.buscarEntidadPorId(
                        request.residenteId()
                );

        ParticipacionActividad participacion =
                new ParticipacionActividad(
                        actividad,
                        residente,
                        request.fecha(),
                        request.asistencia(),
                        request.estado(),
                        request.observaciones()
                );

        return toResponse(
                repository.save(participacion)
        );
    }

    @Transactional(readOnly = true)
    public ParticipacionActividadResponse buscarPorId(Long id) {

        return toResponse(
                buscarEntidadPorId(id)
        );
    }

    @Transactional(readOnly = true)
    public List<ParticipacionActividadResponse> listarPorActividad(
            Long actividadId
    ) {

        actividadService.buscarEntidadPorId(actividadId);

        return repository
                .findByActividadIdOrderByIdAsc(actividadId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ParticipacionActividadResponse> listarPorResidente(
            Long residenteId
    ) {

        residenteService.buscarEntidadPorId(residenteId);

        return repository
                .findByResidenteIdOrderByFechaDesc(residenteId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ParticipacionActividadResponse actualizarAsistencia(
            Long id,
            AsistenciaActividadRequest request
    ) {

        ParticipacionActividad participacion =
                buscarEntidadPorId(id);

        participacion.actualizarAsistencia(
                request.asistencia(),
                request.estado(),
                request.observaciones()
        );

        return toResponse(
                repository.save(participacion)
        );
    }

    @Transactional(readOnly = true)
    public ParticipacionActividad buscarEntidadPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Participación no encontrada con id: " + id
                        )
                );
    }

    private ParticipacionActividadResponse toResponse(
            ParticipacionActividad participacion
    ) {

        return new ParticipacionActividadResponse(
                participacion.getId(),
                participacion.getActividad().getId(),
                participacion.getResidente().getId(),
                participacion.getFecha(),
                participacion.getAsistencia(),
                participacion.getEstado(),
                participacion.getObservaciones()
        );
    }
}