package com.fbecvort.springapirest.entities;

import com.fbecvort.springapirest.enums.TipoCuenta;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cuentaId;

    private long numeroCuenta;
    private TipoCuenta tipoCuenta;
    private BigDecimal saldo;
    private boolean estado;

    @ManyToOne
    @JoinColumn(name = "personaId", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "cuenta")
    private Set<Movimiento> movimientos = new HashSet<>();
}
