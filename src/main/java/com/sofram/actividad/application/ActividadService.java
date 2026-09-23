package com.sofram.actividad.application;

import com.sofram.actividad.domain.Actividad;
import com.sofram.actividad.domain.DetalleCalendario;
import com.sofram.actividad.infrastructure.persistence.ActividadRepository;
import com.sofram.actividad.web.dto.ActividadRequest;
import com.sofram.actividad.web.dto.ActividadResponse;
import com.sofram.personal.application.PersonalService;
import com.sofram.personal.domain.Empleado;
import com.sofram.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final CalendarioService calendarioService;
    private final PersonalService personalService;

    public ActividadService(
            ActividadRepository actividadRepository,
            CalendarioService calendarioService,
            PersonalService personalService
    ) {
        this.actividadRepository = actividadRepository;
        this.calendarioService = calendarioService;
        this.personalService = personalService;
    }

    @Transactional
    public ActividadResponse crear(ActividadRequest request) {

        DetalleCalendario detalle =
                calendarioService.buscarDetalleEntidadPorId(
                        request.detalleCalendarioId()
                );

        Empleado empleado =
                personalService.buscarEmpleadoPorId(
                        request.empleadoId()
                );

        Actividad actividad = new Actividad(
                detalle,
                empleado,
                request.nombre(),
                request.descripcion(),
                request.tipo(),
                request.duracion(),
                request.cupoMaximo(),
                request.estado()
        );

        return toResponse(
                actividadRepository.save(actividad)
        );
    }

    @Transactional(readOnly = true)
    public ActividadResponse buscarPorId(Long id) {

        return toResponse(
                buscarEntidadPorId(id)
        );
    }

    @Transactional(readOnly = true)
    public List<ActividadResponse> listar() {

        return actividadRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ActividadResponse> listarPorDetalleCalendario(
            Long detalleCalendarioId
    ) {

        calendarioService.buscarDetalleEntidadPorId(
                detalleCalendarioId
        );

        return actividadRepository
                .findByDetalleCalendarioIdOrderByIdAsc(
                        detalleCalendarioId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ActividadResponse> listarPorEmpleado(
            Long empleadoId
    ) {

        personalService.buscarEmpleadoPorId(empleadoId);

        return actividadRepository
                .findByEmpleadoIdOrderByIdDesc(empleadoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Actividad buscarEntidadPorId(Long id) {

        return actividadRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Actividad no encontrada con id: " + id
                        )
                );
    }

    private ActividadResponse toResponse(
            Actividad actividad
    ) {

        return new ActividadResponse(
                actividad.getId(),
                actividad.getDetalleCalendario().getId(),
                actividad.getEmpleado().getId(),
                actividad.getNombre(),
                actividad.getDescripcion(),
                actividad.getTipo(),
                actividad.getDuracion(),
                actividad.getCupoMaximo(),
                actividad.getEstado()
        );
    }
}