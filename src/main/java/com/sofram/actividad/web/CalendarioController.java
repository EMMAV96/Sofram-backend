package com.sofram.actividad.web;

import com.sofram.actividad.application.CalendarioService;
import com.sofram.actividad.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendarios")
public class CalendarioController {

    private final CalendarioService calendarioService;

    public CalendarioController(
            CalendarioService calendarioService
    ) {
        this.calendarioService = calendarioService;
    }

    @PostMapping
    public ResponseEntity<CalendarioResponse> crear(
            @Valid @RequestBody CalendarioRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(calendarioService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<CalendarioResponse>> listar() {

        return ResponseEntity.ok(
                calendarioService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalendarioResponse> buscarPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                calendarioService.buscarPorId(id)
        );
    }

    @PostMapping("/{calendarioId}/detalles")
    public ResponseEntity<DetalleCalendarioResponse> agregarDetalle(
            @PathVariable Long calendarioId,
            @Valid @RequestBody DetalleCalendarioRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        calendarioService.agregarDetalle(
                                calendarioId,
                                request
                        )
                );
    }

    @GetMapping("/{calendarioId}/detalles")
    public ResponseEntity<List<DetalleCalendarioResponse>> listarDetalles(
            @PathVariable Long calendarioId
    ) {

        return ResponseEntity.ok(
                calendarioService.listarDetalles(calendarioId)
        );
    }

    @GetMapping("/detalles/{id}")
    public ResponseEntity<DetalleCalendarioResponse> buscarDetalle(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                calendarioService.buscarDetallePorId(id)
        );
    }
}