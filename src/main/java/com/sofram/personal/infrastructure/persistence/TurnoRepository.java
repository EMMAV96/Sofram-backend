package com.sofram.personal.infrastructure.persistence;

import com.sofram.personal.domain.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnoRepository extends JpaRepository<Turno, Long> {
}
