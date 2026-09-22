package com.sofram.gestionmedica.infrastructure.persistence;

import com.sofram.gestionmedica.domain.Tratamiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TratamientoRepository extends JpaRepository<Tratamiento, Long> {

    List<Tratamiento> findByDetalleHistoriaClinicaIdOrderByIdDesc(
            Long detalleHistoriaClinicaId
    );
}