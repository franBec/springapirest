package com.fbecvort.springapirest.dto.cuenta;

import com.fbecvort.springapirest.enumeration.TipoCuenta;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaRequestDTO {

    @NotNull(message = "numeroCuenta no debe ser nulo")
    private long numeroCuenta;

    @NotNull(message = "tipoCuenta no debe ser nulo")
    private TipoCuenta tipoCuenta;

    @NotNull(message = "saldo no debe ser nulo")
    private BigDecimal saldo;

    @NotNull(message = "estado no debe ser nulo")
    private boolean estado;

    @NotNull(message = "clienteId no debe ser nulo, una Cuenta pertenece a un Cliente")
    private Long clienteId;
}
