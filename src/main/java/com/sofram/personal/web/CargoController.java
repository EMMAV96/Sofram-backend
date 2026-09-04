package com.sofram.personal.web;

import com.sofram.personal.application.PersonalService;
import com.sofram.personal.web.dto.CargoRequest;
import com.sofram.personal.web.dto.CargoResponse;
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
@RequestMapping("/personal/cargos")
@Tag(name = "Personal - Cargos", description = "Gestion de cargos laborales")
public class CargoController {

    private final PersonalService personalService;

    public CargoController(PersonalService personalService) {
        this.personalService = personalService;
    }

    @GetMapping
    @Operation(summary = "Listar cargos")
    public ResponseEntity<List<CargoResponse>> listar() {
        return ResponseEntity.ok(personalService.listarCargos());
    }

    @PostMapping
    @Operation(summary = "Crear cargo")
    public ResponseEntity<CargoResponse> crear(@Valid @RequestBody CargoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personalService.crearCargo(request));
    }
}
