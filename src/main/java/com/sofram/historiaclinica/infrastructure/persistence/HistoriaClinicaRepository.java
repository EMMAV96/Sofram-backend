package com.sofram.historiaclinica.infrastructure.persistence;

import com.sofram.historiaclinica.domain.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoriaClinicaRepository
        extends JpaRepository<HistoriaClinica, Long> {

    Optional<HistoriaClinica> findByResidenteId(Long residenteId);

    boolean existsByResidenteId(Long residenteId);
}