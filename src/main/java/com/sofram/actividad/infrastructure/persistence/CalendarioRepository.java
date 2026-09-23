package com.sofram.actividad.infrastructure.persistence;

import com.sofram.actividad.domain.Calendario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarioRepository
        extends JpaRepository<Calendario, Long> {
}