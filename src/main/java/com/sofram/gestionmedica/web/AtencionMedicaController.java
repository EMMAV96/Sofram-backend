package com.sofram.gestionmedica.web;

import com.sofram.gestionmedica.application.AtencionMedicaService;
import com.sofram.gestionmedica.web.dto.AtencionMedicaRequest;
import com.sofram.gestionmedica.web.dto.AtencionMedicaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atenciones-medicas")
public class AtencionMedicaController {

    private final AtencionMedicaService atencionMedicaService;

    public AtencionMedicaController(
            AtencionMedicaService atencionMedicaService
    ) {
        this.atencionMedicaService = atencionMedicaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AtencionMedicaResponse crear(
            @Valid @RequestBody AtencionMedicaRequest request
    ) {
        return atencionMedicaService.crear(request);
    }

    @GetMapping("/{id}")
    public AtencionMedicaResponse buscarPorId(
            @PathVariable Long id
    ) {
        return atencionMedicaService.buscarPorId(id);
    }

    @GetMapping("/residente/{residenteId}")
    public List<AtencionMedicaResponse> listarPorResidente(
            @PathVariable Long residenteId
    ) {
        return atencionMedicaService.listarPorResidente(residenteId);
    }

    @GetMapping("/empleado/{empleadoId}")
    public List<AtencionMedicaResponse> listarPorEmpleado(
            @PathVariable Long empleadoId
    ) {
        return atencionMedicaService.listarPorEmpleado(empleadoId);
    }
}