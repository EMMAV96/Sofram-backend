package com.sofram.residente.web;

import com.sofram.residente.application.HabitacionService;
import com.sofram.residente.web.dto.HabitacionRequest;
import com.sofram.residente.web.dto.HabitacionResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {

    private final HabitacionService habitacionService;

    public HabitacionController(
            HabitacionService habitacionService
    ) {
        this.habitacionService = habitacionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HabitacionResponse crear(
            @Valid @RequestBody HabitacionRequest request
    ) {

        return habitacionService.crear(request);
    }

    @GetMapping
    public List<HabitacionResponse> listar() {

        return habitacionService.listar();
    }

    @GetMapping("/{id}")
    public HabitacionResponse buscarPorId(
            @PathVariable Long id
    ) {

        return habitacionService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public HabitacionResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HabitacionRequest request
    ) {

        return habitacionService.actualizar(
                id,
                request
        );
    }
}