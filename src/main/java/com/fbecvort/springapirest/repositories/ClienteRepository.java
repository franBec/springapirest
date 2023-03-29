package com.fbecvort.springapirest.repositories;

import com.fbecvort.springapirest.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
