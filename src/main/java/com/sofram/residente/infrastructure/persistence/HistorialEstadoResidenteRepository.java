package com.sofram.residente.infrastructure.persistence;

import com.sofram.residente.domain.HistorialEstadoResidente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialEstadoResidenteRepository
        extends JpaRepository<HistorialEstadoResidente, Long> {

    List<HistorialEstadoResidente> findByResidenteIdOrderByFechaCambioDesc(
            Long residenteId
    );
}