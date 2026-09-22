package com.sofram.gestionmedica.infrastructure.persistence;

import com.sofram.gestionmedica.domain.AtencionMedica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtencionMedicaRepository extends JpaRepository<AtencionMedica, Long> {

    List<AtencionMedica> findByResidenteIdOrderByFechaDesc(Long residenteId);

    List<AtencionMedica> findByEmpleadoIdOrderByFechaDesc(Long empleadoId);
}