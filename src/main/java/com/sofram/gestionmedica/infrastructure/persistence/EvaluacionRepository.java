package com.sofram.gestionmedica.infrastructure.persistence;

import com.sofram.gestionmedica.domain.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    Optional<Evaluacion> findByAtencionMedicaId(Long atencionMedicaId);

    List<Evaluacion> findByDetalleHistoriaClinicaIdOrderByIdDesc(Long detalleHistoriaClinicaId);
}