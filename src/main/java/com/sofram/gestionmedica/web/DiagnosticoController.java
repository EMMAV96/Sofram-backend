package com.sofram.gestionmedica.web;


import com.sofram.gestionmedica.application.DiagnosticoService;
import com.sofram.gestionmedica.web.dto.DiagnosticoRequest;
import com.sofram.gestionmedica.web.dto.DiagnosticoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diagnosticos")
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;

    public DiagnosticoController(DiagnosticoService diagnosticoService) {
        this.diagnosticoService = diagnosticoService;
    }

    @PostMapping
    public ResponseEntity<DiagnosticoResponse> crear(
            @Valid @RequestBody DiagnosticoRequest request
    ) {
        DiagnosticoResponse response = diagnosticoService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                diagnosticoService.buscarPorId(id)
        );
    }

    @GetMapping("/detalle-historia/{detalleHistoriaClinicaId}")
    public ResponseEntity<List<DiagnosticoResponse>> listarPorDetalleHistoria(
            @PathVariable Long detalleHistoriaClinicaId
    ) {
        return ResponseEntity.ok(
                diagnosticoService.listarPorDetalleHistoria(
                        detalleHistoriaClinicaId
                )
        );
    }
}