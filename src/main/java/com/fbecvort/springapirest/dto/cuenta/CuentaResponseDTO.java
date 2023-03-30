package com.fbecvort.springapirest.dto.cuenta;

import com.fbecvort.springapirest.enumeration.TipoCuenta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaResponseDTO {
    private Long cuentaId;
    private long numeroCuenta;
    private TipoCuenta tipoCuenta;
    private BigDecimal saldo;
    private boolean estado;
    private Long clienteId;
    private Set<Long> movimientosId;
}
