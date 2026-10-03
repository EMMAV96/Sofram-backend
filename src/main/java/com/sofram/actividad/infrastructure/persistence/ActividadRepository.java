package com.sofram.actividad.infrastructure.persistence;

import com.sofram.actividad.domain.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ActividadRepository
        extends JpaRepository<Actividad, Long> {

    List<Actividad>
    findByDetalleCalendarioIdOrderByIdAsc(Long detalleCalendarioId);

    Optional<Actividad>
    findFirstByDetalleCalendarioIdOrderByIdAsc(Long detalleCalendarioId);

    List<Actividad>
    findByDetalleCalendarioIdInOrderByIdAsc(
            Collection<Long> detalleCalendarioIds
    );

    List<Actividad>
    findByEmpleadoIdOrderByIdDesc(Long empleadoId);
}
