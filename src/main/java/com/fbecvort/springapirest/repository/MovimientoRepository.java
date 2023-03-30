package com.fbecvort.springapirest.repository;

import com.fbecvort.springapirest.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
}
