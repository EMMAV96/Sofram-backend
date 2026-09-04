package com.sofram.personal.web;

import com.sofram.personal.application.PersonalService;
import com.sofram.personal.web.dto.AsignacionTurnoRequest;
import com.sofram.personal.web.dto.AsignacionTurnoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/personal/empleados/{empleadoId}/asignaciones-turno")
@Tag(name = "Personal - Asignaciones de turno", description = "Historial de turnos por empleado")
public class AsignacionTurnoController {

    private final PersonalService personalService;

    public AsignacionTurnoController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @GetMapping
    @Operation(summary = "Listar asignaciones de turno del empleado")
    public ResponseEntity<List<AsignacionTurnoResponse>> listar(@PathVariable Long empleadoId) {
        return ResponseEntity.ok(personalService.listarAsignaciones(empleadoId));
    }

    @PostMapping
    @Operation(summary = "Asignar o cambiar turno vigente del empleado")
    public ResponseEntity<AsignacionTurnoResponse> asignar(
            @PathVariable Long empleadoId,
            @Valid @RequestBody AsignacionTurnoRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personalService.asignarTurno(empleadoId, request));
    }
}
