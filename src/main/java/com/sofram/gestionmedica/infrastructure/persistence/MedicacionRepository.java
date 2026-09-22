package com.sofram.gestionmedica.infrastructure.persistence;

import com.sofram.gestionmedica.domain.Medicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicacionRepository extends JpaRepository<Medicacion, Long> {

    List<Medicacion> findByDetalleHistoriaClinicaIdOrderByIdDesc(
            Long detalleHistoriaClinicaId
    );
}