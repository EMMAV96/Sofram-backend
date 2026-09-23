package com.sofram.auditoria.infrastructure.persistence;

import com.sofram.auditoria.domain.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaRepository
        extends JpaRepository<Auditoria, Long> {

    List<Auditoria> findAllByOrderByFechaHoraDesc();

    List<Auditoria> findByUsuarioIdOrderByFechaHoraDesc(
            Long usuarioId
    );

    List<Auditoria> findByEntidadAndEntidadIdOrderByFechaHoraDesc(
            String entidad,
            Long entidadId
    );
}