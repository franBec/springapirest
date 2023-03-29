package com.fbecvort.springapirest.repositories;

import com.fbecvort.springapirest.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
}
