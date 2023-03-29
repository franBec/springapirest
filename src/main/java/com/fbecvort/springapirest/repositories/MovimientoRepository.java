package com.fbecvort.springapirest.repositories;

import com.fbecvort.springapirest.entities.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
}
