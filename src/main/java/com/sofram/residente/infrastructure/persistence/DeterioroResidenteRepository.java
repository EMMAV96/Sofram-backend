package com.sofram.residente.infrastructure.persistence;

import com.sofram.residente.domain.DeterioroResidente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeterioroResidenteRepository
        extends JpaRepository<DeterioroResidente, Long> {

    boolean existsByNombre(String nombre);
}