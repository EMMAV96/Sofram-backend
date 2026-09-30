package com.sofram.residente.web;

import com.sofram.residente.application.EstadoResidenteService;
import com.sofram.residente.application.ResidenteService;
import com.sofram.residente.web.dto.ActualizarResidenteRequest;
import com.sofram.residente.web.dto.CambioEstadoResidenteRequest;
import com.sofram.residente.web.dto.EstadoResidenteResponse;
import com.sofram.residente.web.dto.EgresoResidenteRequest;
import com.sofram.residente.web.dto.HistorialEstadoResidenteResponse;
import com.sofram.residente.web.dto.ResidenteRequest;
import com.sofram.residente.web.dto.ResidenteResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/residentes")
public class ResidenteController {

    private final ResidenteService residenteService;
    private final EstadoResidenteService estadoResidenteService;

    public ResidenteController(
            ResidenteService residenteService,
            EstadoResidenteService estadoResidenteService
    ) {
        this.residenteService = residenteService;
        this.estadoResidenteService = estadoResidenteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResidenteResponse crear(
            @Valid @RequestBody ResidenteRequest request
    ) {
        return residenteService.crear(request);
    }

    @GetMapping
    public List<ResidenteResponse> listar() {
        return residenteService.listar();
    }

    @GetMapping("/estados")
    public List<EstadoResidenteResponse> listarEstados() {
        return estadoResidenteService.listar();
    }

    @GetMapping("/{id}")
    public ResidenteResponse buscarPorId(
            @PathVariable Long id
    ) {
        return residenteService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ResidenteResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarResidenteRequest request
    ) {
        return residenteService.actualizar(
                id,
                request
        );
    }

    @PutMapping("/{id}/estado")
    public ResidenteResponse cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoResidenteRequest request
    ) {
        return residenteService.cambiarEstado(
                id,
                request
        );
    }

    @PutMapping("/{id}/egreso")
    public ResidenteResponse egresar(
            @PathVariable Long id,
            @Valid @RequestBody EgresoResidenteRequest request
    ) {
        return residenteService.egresar(
                id,
                request
        );
    }

    @GetMapping("/{id}/historial-estados")
    public List<HistorialEstadoResidenteResponse> listarHistorialEstados(
            @PathVariable Long id
    ) {
        return residenteService.listarHistorialEstados(id);
    }
}
