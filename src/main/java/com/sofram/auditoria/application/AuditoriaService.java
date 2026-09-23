package com.sofram.auditoria.application;

import com.sofram.auditoria.domain.Auditoria;
import com.sofram.auditoria.infrastructure.persistence.AuditoriaRepository;
import com.sofram.auditoria.web.dto.AuditoriaResponse;
import com.sofram.auth.application.UsuarioPrincipal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaService(
            AuditoriaRepository auditoriaRepository
    ) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(
            String accion,
            String modulo,
            String entidad,
            Long entidadId,
            String detalle
    ) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal()
                instanceof UsuarioPrincipal principal)) {

            return;
        }

        Auditoria auditoria = new Auditoria(
                principal.getId(),
                principal.getUsername(),
                principal.getRol(),
                accion,
                modulo,
                entidad,
                entidadId,
                detalle
        );

        auditoriaRepository.save(auditoria);
    }

    @Transactional(readOnly = true)
    public List<AuditoriaResponse> listar() {

        return auditoriaRepository
                .findAllByOrderByFechaHoraDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AuditoriaResponse> listarPorUsuario(
            Long usuarioId
    ) {

        return auditoriaRepository
                .findByUsuarioIdOrderByFechaHoraDesc(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AuditoriaResponse> listarPorEntidad(
            String entidad,
            Long entidadId
    ) {

        return auditoriaRepository
                .findByEntidadAndEntidadIdOrderByFechaHoraDesc(
                        entidad,
                        entidadId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AuditoriaResponse toResponse(
            Auditoria auditoria
    ) {

        return new AuditoriaResponse(
                auditoria.getId(),
                auditoria.getUsuarioId(),
                auditoria.getUsername(),
                auditoria.getRol(),
                auditoria.getAccion(),
                auditoria.getModulo(),
                auditoria.getEntidad(),
                auditoria.getEntidadId(),
                auditoria.getDetalle(),
                auditoria.getFechaHora()
        );
    }
}