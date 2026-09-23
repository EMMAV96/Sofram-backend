package com.sofram.actividad.infrastructure.persistence;

import com.sofram.actividad.domain.ParticipacionActividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipacionActividadRepository
        extends JpaRepository<ParticipacionActividad, Long> {

    List<ParticipacionActividad>
    findByActividadIdOrderByIdAsc(Long actividadId);

    List<ParticipacionActividad>
    findByResidenteIdOrderByFechaDesc(Long residenteId);

    boolean existsByActividadIdAndResidenteId(
            Long actividadId,
            Long residenteId
    );
}