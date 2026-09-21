package com.sofram.residente.web;

import com.sofram.residente.application.ResidenteService;
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

    public ResidenteController(
            ResidenteService residenteService
    ) {
        this.residenteService = residenteService;
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

    @GetMapping("/{id}")
    public ResidenteResponse buscarPorId(
            @PathVariable Long id
    ) {
        return residenteService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ResidenteResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ResidenteRequest request
    ) {
        return residenteService.actualizar(
                id,
                request
        );
    }
}