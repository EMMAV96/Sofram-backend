package com.sofram.personal.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EmpleadoRequest(
        @NotNull
        Long cargoId,

        @NotBlank
        @Size(max = 100)
        String apellido,

        @NotBlank
        @Size(max = 100)
        String nombre,

        @NotBlank
        @Size(max = 20)
        String dni,

        @NotNull
        @Past
        LocalDate fechaNacimiento,

        @Size(max = 255)
        String direccion,

        @Size(max = 30)
        String telefono,

        @Email
        @Size(max = 150)
        String email
) {
}
