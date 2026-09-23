package com.sofram.actividad.infrastructure.persistence;

import com.sofram.actividad.domain.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActividadRepository
        extends JpaRepository<Actividad, Long> {

    List<Actividad>
    findByDetalleCalendarioIdOrderByIdAsc(Long detalleCalendarioId);

    List<Actividad>
    findByEmpleadoIdOrderByIdDesc(Long empleadoId);
}