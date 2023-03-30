package com.fbecvort.springapirest.dto.reporte;

import com.fbecvort.springapirest.enumeration.TipoCuenta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteCuentaResponseDTO {
    private Long cuentaId;
    private long numeroCuenta;
    private TipoCuenta tipoCuenta;
    private BigDecimal saldo;
    private boolean estado;

    private Date periodoStart;
    private Date periodoEnd;

    private BigDecimal totalRetirosEnPeriodo;
    private BigDecimal totalDepositosEnPeriodo;
}
