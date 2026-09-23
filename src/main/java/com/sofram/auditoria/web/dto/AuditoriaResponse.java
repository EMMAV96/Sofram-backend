package com.sofram.auditoria.web.dto;

import java.time.LocalDateTime;

public record AuditoriaResponse(
        Long id,
        Long usuarioId,
        String username,
        String rol,
        String accion,
        String modulo,
        String entidad,
        Long entidadId,
        String detalle,
        LocalDateTime fechaHora
) {
}