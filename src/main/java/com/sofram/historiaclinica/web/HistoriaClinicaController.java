package com.sofram.historiaclinica.web;

import com.sofram.historiaclinica.application.HistoriaClinicaService;
import com.sofram.historiaclinica.web.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historias-clinicas")
public class HistoriaClinicaController {

    private final HistoriaClinicaService historiaClinicaService;

    public HistoriaClinicaController(
            HistoriaClinicaService historiaClinicaService
    ) {
        this.historiaClinicaService = historiaClinicaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HistoriaClinicaResponse crear(
            @Valid @RequestBody HistoriaClinicaRequest request
    ) {
        return historiaClinicaService.crear(request);
    }

    @GetMapping("/residente/{residenteId}")
    public HistoriaClinicaResponse buscarPorResidente(
            @PathVariable Long residenteId
    ) {
        return historiaClinicaService.buscarPorResidente(residenteId);
    }

    @GetMapping("/{id}")
    public HistoriaClinicaResponse buscarPorId(
            @PathVariable Long id
    ) {
        return historiaClinicaService.buscarPorId(id);
    }

    @PostMapping("/{historiaClinicaId}/detalles")
    @ResponseStatus(HttpStatus.CREATED)
    public DetalleHistoriaClinicaResponse agregarDetalle(
            @PathVariable Long historiaClinicaId,
            @Valid @RequestBody DetalleHistoriaClinicaRequest request
    ) {
        return historiaClinicaService.agregarDetalle(
                historiaClinicaId,
                request
        );
    }

    @GetMapping("/{historiaClinicaId}/detalles")
    public List<DetalleHistoriaClinicaResponse> listarDetalles(
            @PathVariable Long historiaClinicaId
    ) {
        return historiaClinicaService.listarDetalles(historiaClinicaId);
    }

    @PutMapping("/{id}/antecedentes-alergias")
    public ResponseEntity<HistoriaClinicaResponse> actualizarAntecedentesYAlergias(
            @PathVariable Long id,
            @Valid @RequestBody AntecedentesHistoriaClinicaRequest request
    ) {

        return ResponseEntity.ok(
                historiaClinicaService.actualizarAntecedentesYAlergias(
                        id,
                        request
                )
        );
    }

}