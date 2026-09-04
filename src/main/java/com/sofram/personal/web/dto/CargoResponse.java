package com.sofram.personal.web.dto;

public record CargoResponse(
        Long id,
        String nombre,
        String sector,
        String matricula,
        String especialidad
) {
}
