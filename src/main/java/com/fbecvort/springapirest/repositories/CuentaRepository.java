package com.fbecvort.springapirest.repositories;

import com.fbecvort.springapirest.entities.Cliente;
import com.fbecvort.springapirest.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    List<Cuenta> findAllByCliente(Cliente cliente);
}
