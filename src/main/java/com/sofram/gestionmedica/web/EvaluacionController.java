package com.sofram.gestionmedica.web;

import com.sofram.gestionmedica.application.EvaluacionService;
import com.sofram.gestionmedica.web.dto.EvaluacionRequest;
import com.sofram.gestionmedica.web.dto.EvaluacionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluaciones")
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    public EvaluacionController(
            EvaluacionService evaluacionService
    ) {
        this.evaluacionService = evaluacionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EvaluacionResponse crear(
            @Valid @RequestBody EvaluacionRequest request
    ) {
        return evaluacionService.crear(request);
    }

    @GetMapping("/{id}")
    public EvaluacionResponse buscarPorId(
            @PathVariable Long id
    ) {
        return evaluacionService.buscarPorId(id);
    }

    @GetMapping("/atencion/{atencionMedicaId}")
    public EvaluacionResponse buscarPorAtencion(
            @PathVariable Long atencionMedicaId
    ) {
        return evaluacionService.buscarPorAtencion(atencionMedicaId);
    }

    @GetMapping("/detalle-historia/{detalleHistoriaClinicaId}")
    public List<EvaluacionResponse> listarPorDetalleHistoria(
            @PathVariable Long detalleHistoriaClinicaId
    ) {
        return evaluacionService.listarPorDetalleHistoria(
                detalleHistoriaClinicaId
        );
    }
}