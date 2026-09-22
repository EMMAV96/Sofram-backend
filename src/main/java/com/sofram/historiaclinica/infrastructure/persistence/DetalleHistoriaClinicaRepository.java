package com.sofram.historiaclinica.infrastructure.persistence;

import com.sofram.historiaclinica.domain.DetalleHistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleHistoriaClinicaRepository
        extends JpaRepository<DetalleHistoriaClinica, Long> {

    List<DetalleHistoriaClinica> findByHistoriaClinicaIdOrderByFechaDesc(
            Long historiaClinicaId
    );
}