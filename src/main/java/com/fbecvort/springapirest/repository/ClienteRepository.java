package com.fbecvort.springapirest.repository;

import com.fbecvort.springapirest.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
