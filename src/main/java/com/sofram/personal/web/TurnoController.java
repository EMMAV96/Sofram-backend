package com.sofram.personal.web;

import com.sofram.personal.application.PersonalService;
import com.sofram.personal.web.dto.TurnoRequest;
import com.sofram.personal.web.dto.TurnoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/personal/turnos")
@Tag(name = "Personal - Turnos", description = "Catalogo de turnos")
public class TurnoController {

    private final PersonalService personalService;

    public TurnoController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @GetMapping
    @Operation(summary = "Listar turnos")
    public ResponseEntity<List<TurnoResponse>> listar() {
        return ResponseEntity.ok(personalService.listarTurnos());
    }

    @PostMapping
    @Operation(summary = "Crear turno")
    public ResponseEntity<TurnoResponse> crear(@Valid @RequestBody TurnoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personalService.crearTurno(request));
    }
}
