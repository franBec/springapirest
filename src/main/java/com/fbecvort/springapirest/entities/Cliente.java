package com.fbecvort.springapirest.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class Cliente extends Persona {

    private String contrasena;
    private boolean estado;

    @OneToMany(mappedBy ="cliente")
    private Set<Cuenta> cuentas = new HashSet<>();
    }
