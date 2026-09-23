package com.sofram.actividad.infrastructure.persistence;

import com.sofram.actividad.domain.DetalleCalendario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleCalendarioRepository
        extends JpaRepository<DetalleCalendario, Long> {

    List<DetalleCalendario>
    findByCalendarioIdOrderByFechaAscHoraInicioAsc(Long calendarioId);
}