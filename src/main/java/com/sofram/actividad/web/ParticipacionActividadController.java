package com.sofram.actividad.web;

import com.sofram.actividad.application.ParticipacionActividadService;
import com.sofram.actividad.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participaciones-actividades")
public class ParticipacionActividadController {

    private final ParticipacionActividadService service;

    public ParticipacionActividadController(
            ParticipacionActividadService service
    ) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ParticipacionActividadResponse> registrar(
            @Valid @RequestBody ParticipacionActividadRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.registrar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipacionActividadResponse> buscarPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                service.buscarPorId(id)
        );
    }

    @GetMapping("/actividad/{actividadId}")
    public ResponseEntity<List<ParticipacionActividadResponse>>
    listarPorActividad(
            @PathVariable Long actividadId
    ) {

        return ResponseEntity.ok(
                service.listarPorActividad(actividadId)
        );
    }

    @GetMapping("/residente/{residenteId}")
    public ResponseEntity<List<ParticipacionActividadResponse>>
    listarPorResidente(
            @PathVariable Long residenteId
    ) {

        return ResponseEntity.ok(
                service.listarPorResidente(residenteId)
        );
    }

    @PutMapping("/{id}/asistencia")
    public ResponseEntity<ParticipacionActividadResponse>
    actualizarAsistencia(
            @PathVariable Long id,
            @Valid @RequestBody AsistenciaActividadRequest request
    ) {

        return ResponseEntity.ok(
                service.actualizarAsistencia(id, request)
        );
    }
}