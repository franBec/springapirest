package com.fbecvort.springapirest.repository;

import com.fbecvort.springapirest.entity.Cliente;
import com.fbecvort.springapirest.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    List<Cuenta> findAllByCliente(Cliente cliente);
}
