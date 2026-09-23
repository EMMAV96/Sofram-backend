package com.sofram.residente.infrastructure.persistence;

import com.sofram.residente.domain.Residente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResidenteRepository
        extends JpaRepository<Residente, Long> {

    Optional<Residente> findByDni(String dni);

    boolean existsByDni(String dni);

    List<Residente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
            String nombre,
            String apellido
    );

    long countByHabitacionIdAndFechaEgresoIsNull(Long habitacionId);
}