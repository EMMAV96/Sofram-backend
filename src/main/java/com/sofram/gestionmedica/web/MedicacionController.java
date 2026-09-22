package com.sofram.gestionmedica.web;

import com.sofram.gestionmedica.application.MedicacionService;
import com.sofram.gestionmedica.web.dto.MedicacionRequest;
import com.sofram.gestionmedica.web.dto.MedicacionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicaciones")
public class MedicacionController {

    private final MedicacionService medicacionService;

    public MedicacionController(MedicacionService medicacionService) {
        this.medicacionService = medicacionService;
    }

    @PostMapping
    public ResponseEntity<MedicacionResponse> crear(
            @Valid @RequestBody MedicacionRequest request
    ) {
        MedicacionResponse response = medicacionService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicacionResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                medicacionService.buscarPorId(id)
        );
    }

    @GetMapping("/detalle-historia/{detalleHistoriaClinicaId}")
    public ResponseEntity<List<MedicacionResponse>> listarPorDetalleHistoria(
            @PathVariable Long detalleHistoriaClinicaId
    ) {
        return ResponseEntity.ok(
                medicacionService.listarPorDetalleHistoria(
                        detalleHistoriaClinicaId
                )
        );
    }
}