package com.sofram.historiaclinica.web.dto;

import jakarta.validation.constraints.Size;

public record AntecedentesHistoriaClinicaRequest(

        @Size(max = 2000)
        String antecedentesPersonales,

        @Size(max = 2000)
        String antecedentesFamiliares,

        @Size(max = 2000)
        String alergias

) {
}