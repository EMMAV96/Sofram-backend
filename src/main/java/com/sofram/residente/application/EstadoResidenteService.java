package com.sofram.residente.application;

import com.sofram.residente.domain.EstadoResidente;
import com.sofram.residente.infrastructure.persistence.EstadoResidenteRepository;
import com.sofram.residente.web.dto.EstadoResidenteResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstadoResidenteService {

    private final EstadoResidenteRepository estadoResidenteRepository;

    public EstadoResidenteService(
            EstadoResidenteRepository estadoResidenteRepository
    ) {
        this.estadoResidenteRepository = estadoResidenteRepository;
    }

    @Transactional(readOnly = true)
    public List<EstadoResidenteResponse> listar() {

        return estadoResidenteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private EstadoResidenteResponse toResponse(
            EstadoResidente estadoResidente
    ) {

        return new EstadoResidenteResponse(
                estadoResidente.getId(),
                estadoResidente.getNombre()
        );
    }
}
