package com.sofram.residente.web.dto;

import java.time.LocalDate;

public record ResidenteResponse(

        Long id,

        String nombre,

        String apellido,

        String dni,

        LocalDate fechaNacimiento,

        String direccion,

        String telefono,

        String email,

        String telefonoEmergencia,

        String familiarACargo,

        LocalDate fechaIngreso,

        LocalDate fechaEgreso,

        String obraSocial,

        Long habitacionId,

        String habitacionNumero,

        String estadoActual
) {
}