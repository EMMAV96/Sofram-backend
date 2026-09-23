package com.sofram.residente.application;

import com.sofram.auditoria.application.AuditoriaService;
import com.sofram.residente.domain.EstadoResidente;
import com.sofram.residente.domain.Habitacion;
import com.sofram.residente.domain.HistorialEstadoResidente;
import com.sofram.residente.domain.Residente;
import com.sofram.residente.infrastructure.persistence.EstadoResidenteRepository;
import com.sofram.residente.infrastructure.persistence.HabitacionRepository;
import com.sofram.residente.infrastructure.persistence.HistorialEstadoResidenteRepository;
import com.sofram.residente.infrastructure.persistence.ResidenteRepository;
import com.sofram.residente.web.dto.ActualizarResidenteRequest;
import com.sofram.residente.web.dto.CambioEstadoResidenteRequest;
import com.sofram.residente.web.dto.HistorialEstadoResidenteResponse;
import com.sofram.residente.web.dto.ResidenteRequest;
import com.sofram.residente.web.dto.ResidenteResponse;

import com.sofram.shared.exception.DuplicateResourceException;
import com.sofram.shared.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResidenteService {

    private final ResidenteRepository residenteRepository;
    private final HabitacionRepository habitacionRepository;
    private final EstadoResidenteRepository estadoRepository;
    private final HistorialEstadoResidenteRepository historialEstadoRepository;
    private final ResidenteMapper residenteMapper;
    private final AuditoriaService auditoriaService;

    public ResidenteService(
            ResidenteRepository residenteRepository,
            HabitacionRepository habitacionRepository,
            EstadoResidenteRepository estadoRepository,
            HistorialEstadoResidenteRepository historialEstadoRepository,
            ResidenteMapper residenteMapper,
            AuditoriaService auditoriaService
    ) {
        this.residenteRepository = residenteRepository;
        this.habitacionRepository = habitacionRepository;
        this.estadoRepository = estadoRepository;
        this.historialEstadoRepository = historialEstadoRepository;
        this.residenteMapper = residenteMapper;
        this.auditoriaService = auditoriaService;
    }

    @Transactional
    public ResidenteResponse crear(
            ResidenteRequest request
    ) {

        if (residenteRepository.existsByDni(request.dni())) {
            throw new DuplicateResourceException(
                    "Ya existe un residente con DNI "
                            + request.dni()
            );
        }

        Habitacion habitacion =
                habitacionRepository.findById(
                        request.habitacionId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Habitación no encontrada"
                        )
                );

        // V14: comprobar capacidad antes de asignar.
        validarCapacidadHabitacion(habitacion);

        EstadoResidente estado =
                estadoRepository.findById(
                        request.estadoInicialId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Estado de residente no encontrado"
                        )
                );

        Residente residente = new Residente();

        residente.setNombre(request.nombre());
        residente.setApellido(request.apellido());
        residente.setDni(request.dni());
        residente.setFechaNacimiento(request.fechaNacimiento());
        residente.setDireccion(request.direccion());
        residente.setTelefono(request.telefono());
        residente.setEmail(request.email());
        residente.setTelefonoEmergencia(
                request.telefonoEmergencia()
        );
        residente.setFamiliarACargo(
                request.familiarACargo()
        );
        residente.setFechaIngreso(
                request.fechaIngreso()
        );
        residente.setFechaEgreso(
                request.fechaEgreso()
        );
        residente.setObraSocial(
                request.obraSocial()
        );
        residente.setHabitacion(habitacion);

        Residente guardado =
                residenteRepository.save(residente);

        HistorialEstadoResidente historial =
                new HistorialEstadoResidente();

        historial.setResidente(guardado);
        historial.setEstado(estado);
        historial.setFechaCambio(
                request.fechaIngreso()
        );
        historial.setObservacion(
                "Estado inicial del residente"
        );

        historialEstadoRepository.save(historial);

        auditoriaService.registrar(
                "CREAR",
                "RESIDENTES",
                "Residente",
                guardado.getId(),
                "Admisión de residente"
        );

        return residenteMapper.toResponse(
                guardado,
                estado.getNombre()
        );
    }

    @Transactional(readOnly = true)
    public List<ResidenteResponse> listar() {

        return residenteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResidenteResponse buscarPorId(Long id) {

        Residente residente =
                residenteRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No existe un residente con id "
                                                + id
                                )
                        );

        return toResponse(residente);
    }

    @Transactional
    public ResidenteResponse actualizar(
            Long id,
            ActualizarResidenteRequest request
    ) {

        Residente residente =
                residenteRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No existe un residente con id "
                                                + id
                                )
                        );

        residenteRepository.findByDni(request.dni())
                .filter(otro ->
                        !otro.getId().equals(id)
                )
                .ifPresent(otro -> {
                    throw new DuplicateResourceException(
                            "Ya existe un residente con DNI "
                                    + request.dni()
                    );
                });

        Habitacion habitacion =
                habitacionRepository.findById(
                        request.habitacionId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Habitación no encontrada"
                        )
                );

        boolean cambiaHabitacion =
                !residente.getHabitacion()
                        .getId()
                        .equals(habitacion.getId());

        /*
         * Solo comprobamos capacidad cuando el residente
         * realmente cambia a otra habitación.
         *
         * Si el residente quedará egresado, no ocupará
         * una plaza actual.
         */
        if (cambiaHabitacion && request.fechaEgreso() == null) {
            validarCapacidadHabitacion(habitacion);
        }

        residente.setNombre(request.nombre());
        residente.setApellido(request.apellido());
        residente.setDni(request.dni());
        residente.setFechaNacimiento(
                request.fechaNacimiento()
        );
        residente.setDireccion(request.direccion());
        residente.setTelefono(request.telefono());
        residente.setEmail(request.email());
        residente.setTelefonoEmergencia(
                request.telefonoEmergencia()
        );
        residente.setFamiliarACargo(
                request.familiarACargo()
        );
        residente.setFechaIngreso(
                request.fechaIngreso()
        );
        residente.setFechaEgreso(
                request.fechaEgreso()
        );
        residente.setObraSocial(
                request.obraSocial()
        );
        residente.setHabitacion(habitacion);

        Residente actualizado =
                residenteRepository.save(residente);

        auditoriaService.registrar(
                "ACTUALIZAR",
                "RESIDENTES",
                "Residente",
                actualizado.getId(),
                "Actualización de datos del residente"
        );

        return toResponse(actualizado);
    }

    private ResidenteResponse toResponse(
            Residente residente
    ) {

        String estadoActual =
                historialEstadoRepository
                        .findByResidenteIdOrderByFechaCambioDesc(
                                residente.getId()
                        )
                        .stream()
                        .findFirst()
                        .map(historial ->
                                historial.getEstado().getNombre()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "El residente no tiene un estado registrado"
                                )
                        );

        return residenteMapper.toResponse(
                residente,
                estadoActual
        );
    }

    @Transactional(readOnly = true)
    public Residente buscarEntidadPorId(Long id) {

        return residenteRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Residente no encontrado con id: "
                                        + id
                        )
                );
    }

    @Transactional
    public ResidenteResponse cambiarEstado(
            Long residenteId,
            CambioEstadoResidenteRequest request
    ) {

        Residente residente =
                residenteRepository.findById(residenteId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No existe un residente con id "
                                                + residenteId
                                )
                        );

        EstadoResidente estado =
                estadoRepository.findById(request.estadoId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Estado de residente no encontrado"
                                )
                        );

        HistorialEstadoResidente historial =
                new HistorialEstadoResidente();

        historial.setResidente(residente);
        historial.setEstado(estado);
        historial.setFechaCambio(request.fechaCambio());
        historial.setObservacion(request.observacion());

        historialEstadoRepository.save(historial);

        auditoriaService.registrar(
                "CAMBIAR_ESTADO",
                "RESIDENTES",
                "Residente",
                residente.getId(),
                "Nuevo estado: " + estado.getNombre()
        );

        /*
         * Volvemos a calcular el estado actual desde el historial.
         * Así evitamos devolver un estado incorrecto si se registra
         * un cambio histórico con una fecha anterior.
         */
        return toResponse(residente);
    }

    @Transactional(readOnly = true)
    public List<HistorialEstadoResidenteResponse> listarHistorialEstados(
            Long residenteId
    ) {

        buscarEntidadPorId(residenteId);

        return historialEstadoRepository
                .findByResidenteIdOrderByFechaCambioDesc(
                        residenteId
                )
                .stream()
                .map(historial ->
                        new HistorialEstadoResidenteResponse(
                                historial.getId(),
                                historial.getResidente().getId(),
                                historial.getEstado().getId(),
                                historial.getEstado().getNombre(),
                                historial.getFechaCambio(),
                                historial.getObservacion()
                        )
                )
                .toList();
    }

    private void validarCapacidadHabitacion(
            Habitacion habitacion
    ) {

        long ocupacionActual =
                residenteRepository
                        .countByHabitacionIdAndFechaEgresoIsNull(
                                habitacion.getId()
                        );

        if (ocupacionActual >= habitacion.getCapacidad()) {
            throw new IllegalArgumentException(
                    "La habitación "
                            + habitacion.getNumero()
                            + " no tiene cupos disponibles"
            );
        }
    }
}
