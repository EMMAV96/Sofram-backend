package com.sofram.residente.infrastructure.persistence;

import com.sofram.residente.domain.EstadoResidente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoResidenteRepository
        extends JpaRepository<EstadoResidente, Long> {

    Optional<EstadoResidente> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}