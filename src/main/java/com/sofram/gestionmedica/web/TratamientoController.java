package com.sofram.gestionmedica.web;

import com.sofram.gestionmedica.application.TratamientoService;
import com.sofram.gestionmedica.web.dto.TratamientoRequest;
import com.sofram.gestionmedica.web.dto.TratamientoResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tratamientos")
public class TratamientoController {

    private final TratamientoService tratamientoService;

    public TratamientoController(TratamientoService tratamientoService) {
        this.tratamientoService = tratamientoService;
    }

    @PostMapping
    public ResponseEntity<TratamientoResponse> crear(
            @Valid @RequestBody TratamientoRequest request
    ) {
        TratamientoResponse response = tratamientoService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TratamientoResponse> buscarPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                tratamientoService.buscarPorId(id)
        );
    }

    @GetMapping("/detalle-historia/{detalleHistoriaClinicaId}")
    public ResponseEntity<List<TratamientoResponse>> listarPorDetalleHistoria(
            @PathVariable Long detalleHistoriaClinicaId
    ) {
        return ResponseEntity.ok(
                tratamientoService.listarPorDetalleHistoria(
                        detalleHistoriaClinicaId
                )
        );
    }
}