package com.sofram.actividad.web;

import com.sofram.actividad.application.ActividadService;
import com.sofram.actividad.web.dto.ActividadRequest;
import com.sofram.actividad.web.dto.ActividadResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actividades")
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(
            ActividadService actividadService
    ) {
        this.actividadService = actividadService;
    }

    @PostMapping
    public ResponseEntity<ActividadResponse> crear(
            @Valid @RequestBody ActividadRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actividadService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<ActividadResponse>> listar() {

        return ResponseEntity.ok(
                actividadService.listar()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActividadResponse> buscarPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                actividadService.buscarPorId(id)
        );
    }

    @GetMapping("/detalle-calendario/{detalleCalendarioId}")
    public ResponseEntity<List<ActividadResponse>>
    listarPorDetalleCalendario(
            @PathVariable Long detalleCalendarioId
    ) {

        return ResponseEntity.ok(
                actividadService.listarPorDetalleCalendario(
                        detalleCalendarioId
                )
        );
    }

    @GetMapping("/empleado/{empleadoId}")
    public ResponseEntity<List<ActividadResponse>> listarPorEmpleado(
            @PathVariable Long empleadoId
    ) {

        return ResponseEntity.ok(
                actividadService.listarPorEmpleado(empleadoId)
        );
    }
}