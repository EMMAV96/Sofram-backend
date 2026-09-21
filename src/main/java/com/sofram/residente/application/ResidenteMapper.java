package com.sofram.residente.application;

import com.sofram.residente.domain.Residente;
import com.sofram.residente.web.dto.ResidenteResponse;
import org.springframework.stereotype.Component;

@Component
public class ResidenteMapper {

    public ResidenteResponse toResponse(
            Residente residente,
            String estadoActual
    ) {

        return new ResidenteResponse(
                residente.getId(),
                residente.getNombre(),
                residente.getApellido(),
                residente.getDni(),
                residente.getFechaNacimiento(),
                residente.getDireccion(),
                residente.getTelefono(),
                residente.getEmail(),
                residente.getTelefonoEmergencia(),
                residente.getFamiliarACargo(),
                residente.getFechaIngreso(),
                residente.getFechaEgreso(),
                residente.getObraSocial(),
                residente.getHabitacion().getId(),
                residente.getHabitacion().getNumero(),
                estadoActual
        );
    }
}