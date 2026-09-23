package com.sofram.auditoria.web;

import com.sofram.auditoria.application.AuditoriaService;
import com.sofram.auditoria.web.dto.AuditoriaResponse;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditorias")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(
            AuditoriaService auditoriaService
    ) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public List<AuditoriaResponse> listar() {
        return auditoriaService.listar();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<AuditoriaResponse> listarPorUsuario(
            @PathVariable Long usuarioId
    ) {
        return auditoriaService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/entidad/{entidad}/{entidadId}")
    public List<AuditoriaResponse> listarPorEntidad(
            @PathVariable String entidad,
            @PathVariable Long entidadId
    ) {
        return auditoriaService.listarPorEntidad(
                entidad,
                entidadId
        );
    }
}