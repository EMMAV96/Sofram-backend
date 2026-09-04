package com.sofram.personal.web.dto;

import java.time.LocalDate;

public record EmpleadoResponse(
        Long id,
        Long cargoId,
        String cargoNombre,
        String apellido,
        String nombre,
        String dni,
        LocalDate fechaNacimiento,
        String direccion,
        String telefono,
        String email,
        LocalDate fechaBaja,
        boolean activo
) {
}
