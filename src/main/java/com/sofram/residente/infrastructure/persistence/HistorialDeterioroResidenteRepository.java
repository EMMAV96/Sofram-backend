package com.sofram.residente.infrastructure.persistence;

import com.sofram.residente.domain.HistorialDeterioroResidente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialDeterioroResidenteRepository
        extends JpaRepository<HistorialDeterioroResidente, Long> {

    List<HistorialDeterioroResidente> findByResidenteIdOrderByFechaCambioDesc(
            Long residenteId
    );
}