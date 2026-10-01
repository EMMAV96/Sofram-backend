package com.sofram.personal.infrastructure.persistence;

import com.sofram.personal.domain.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

    Optional<Cargo> findFirstByNombreAndSectorAndEspecialidad(
            String nombre,
            String sector,
            String especialidad
    );
}
