package com.fbecvort.springapirest.entities;

import com.fbecvort.springapirest.enums.TipoMovimiento;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movimientoId;

    @Builder.Default
    private Date fecha = new Date();

    private TipoMovimiento tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldoInicial;

    @ManyToOne
    @JoinColumn(name = "cuentaId", nullable = false)
    private Cuenta cuenta;
}
