package com.sofram.residente.application;

import com.sofram.residente.domain.Habitacion;
import com.sofram.residente.infrastructure.persistence.HabitacionRepository;
import com.sofram.residente.web.dto.HabitacionRequest;
import com.sofram.residente.web.dto.HabitacionResponse;
import com.sofram.shared.exception.DuplicateResourceException;
import com.sofram.shared.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;

    public HabitacionService(
            HabitacionRepository habitacionRepository
    ) {
        this.habitacionRepository = habitacionRepository;
    }

    @Transactional
    public HabitacionResponse crear(
            HabitacionRequest request
    ) {

        if (habitacionRepository.existsByNumero(
                request.numero()
        )) {

            throw new DuplicateResourceException(
                    "Ya existe una habitación con número "
                            + request.numero()
            );
        }

        Habitacion habitacion = new Habitacion();

        habitacion.setNumero(request.numero());
        habitacion.setCapacidad(request.capacidad());
        habitacion.setTipo(request.tipo());
        habitacion.setEstado(request.estado());

        Habitacion guardada =
                habitacionRepository.save(habitacion);

        return toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<HabitacionResponse> listar() {

        return habitacionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public HabitacionResponse buscarPorId(Long id) {

        Habitacion habitacion =
                habitacionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No existe una habitación con id "
                                                + id
                                )
                        );

        return toResponse(habitacion);
    }

    @Transactional
    public HabitacionResponse actualizar(
            Long id,
            HabitacionRequest request
    ) {

        Habitacion habitacion =
                habitacionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No existe una habitación con id "
                                                + id
                                )
                        );

        habitacionRepository.findByNumero(request.numero())
                .filter(otra ->
                        !otra.getId().equals(id)
                )
                .ifPresent(otra -> {
                    throw new DuplicateResourceException(
                            "Ya existe una habitación con número "
                                    + request.numero()
                    );
                });

        habitacion.setNumero(request.numero());
        habitacion.setCapacidad(request.capacidad());
        habitacion.setTipo(request.tipo());
        habitacion.setEstado(request.estado());

        Habitacion actualizada =
                habitacionRepository.save(habitacion);

        return toResponse(actualizada);
    }

    private HabitacionResponse toResponse(
            Habitacion habitacion
    ) {

        return new HabitacionResponse(
                habitacion.getId(),
                habitacion.getNumero(),
                habitacion.getCapacidad(),
                habitacion.getTipo(),
                habitacion.getEstado()
        );
    }
}