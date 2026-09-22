package com.sofram.gestionmedica.infrastructure.persistence;

import com.sofram.gestionmedica.domain.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    List<Diagnostico> findByDetalleHistoriaClinicaIdOrderByIdDesc(
            Long detalleHistoriaClinicaId
    );
}