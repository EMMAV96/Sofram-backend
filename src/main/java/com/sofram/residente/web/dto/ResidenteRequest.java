package com.sofram.residente.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ResidenteRequest(

        @NotBlank
        @Size(max = 100)
        String nombre,

        @NotBlank
        @Size(max = 100)
        String apellido,

        @NotBlank
        @Size(max = 20)
        String dni,

        @NotNull
        LocalDate fechaNacimiento,

        @Size(max = 255)
        String direccion,

        @Size(max = 30)
        String telefono,

        @Email
        @Size(max = 150)
        String email,

        @Size(max = 30)
        String telefonoEmergencia,

        @Size(max = 150)
        String familiarACargo,

        @NotNull
        LocalDate fechaIngreso,

        LocalDate fechaEgreso,

        @Size(max = 100)
        String obraSocial,

        @NotNull
        Long habitacionId,

        @NotNull
        Long estadoInicialId
) {
}