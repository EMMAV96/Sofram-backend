package com.sofram.personal.web;

import com.sofram.personal.application.PersonalService;
import com.sofram.personal.web.dto.BajaEmpleadoRequest;
import com.sofram.personal.web.dto.EmpleadoRequest;
import com.sofram.personal.web.dto.EmpleadoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/personal/empleados")
@Tag(name = "Personal - Empleados", description = "Gestion de empleados")
public class EmpleadoController {

    private final PersonalService personalService;

    public EmpleadoController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @GetMapping
    @Operation(summary = "Listar empleados")
    public ResponseEntity<List<EmpleadoResponse>> listar() {
        return ResponseEntity.ok(personalService.listarEmpleados());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener empleado por ID")
    public ResponseEntity<EmpleadoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(personalService.obtenerEmpleado(id));
    }

    @PostMapping
    @Operation(summary = "Crear empleado")
    public ResponseEntity<EmpleadoResponse> crear(@Valid @RequestBody EmpleadoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personalService.crearEmpleado(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar empleado")
    public ResponseEntity<EmpleadoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpleadoRequest request
    ) {
        return ResponseEntity.ok(personalService.actualizarEmpleado(id, request));
    }

    @PatchMapping("/{id}/baja")
    @Operation(summary = "Dar de baja empleado")
    public ResponseEntity<EmpleadoResponse> darDeBaja(
            @PathVariable Long id,
            @Valid @RequestBody BajaEmpleadoRequest request
    ) {
        return ResponseEntity.ok(personalService.darDeBajaEmpleado(id, request));
    }
}
