package com.fbecvort.springapirest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @Builder.Default
    @OneToMany(mappedBy ="cliente")
    private Set<Cuenta> cuentas = new HashSet<>();
    }
