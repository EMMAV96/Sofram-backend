package com.sofram.residente.application;

import com.sofram.residente.domain.*;
import com.sofram.residente.infrastructure.persistence.*;
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

    public ResidenteService(
            ResidenteRepository residenteRepository,
            HabitacionRepository habitacionRepository,
            EstadoResidenteRepository estadoRepository,
            HistorialEstadoResidenteRepository historialEstadoRepository,
            ResidenteMapper residenteMapper
    ) {
        this.residenteRepository = residenteRepository;
        this.habitacionRepository = habitacionRepository;
        this.estadoRepository = estadoRepository;
        this.historialEstadoRepository = historialEstadoRepository;
        this.residenteMapper = residenteMapper;
    }

    @Transactional
    public ResidenteResponse crear(ResidenteRequest request) {

        if (residenteRepository.existsByDni(request.dni())) {
            throw new DuplicateResourceException(
                    "Ya existe un residente con DNI " + request.dni()
            );
        }

        Habitacion habitacion = habitacionRepository
                .findById(request.habitacionId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Habitación no encontrada"
                        )
                );

        EstadoResidente estado = estadoRepository
                .findById(request.estadoInicialId())
                .orElseThrow(() ->
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
            ResidenteRequest request
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
}