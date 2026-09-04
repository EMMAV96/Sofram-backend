package com.sofram.personal.infrastructure.persistence;

import com.sofram.personal.domain.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
}
