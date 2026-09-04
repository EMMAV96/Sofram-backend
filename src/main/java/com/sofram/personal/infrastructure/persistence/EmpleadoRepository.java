package com.sofram.personal.infrastructure.persistence;

import com.sofram.personal.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    boolean existsByDni(String dni);

    Optional<Empleado> findByDni(String dni);
}
