package com.sofram.personal.infrastructure.persistence;

import com.sofram.personal.domain.AsignacionTurno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsignacionTurnoRepository extends JpaRepository<AsignacionTurno, Long> {

    List<AsignacionTurno> findByEmpleadoIdOrderByFechaDesdeDesc(Long empleadoId);

    Optional<AsignacionTurno> findByEmpleadoIdAndFechaHastaIsNull(Long empleadoId);
}
