package com.sofram.residente.web.dto;

import java.time.LocalDate;

public record HistorialEstadoResidenteResponse(

        Long id,
        Long residenteId,
        Long estadoId,
        String estado,
        LocalDate fechaCambio,
        String observacion

) {
}