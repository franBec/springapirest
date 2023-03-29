package com.fbecvort.springapirest.dtos.movimiento;

import com.fbecvort.springapirest.enums.TipoMovimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MovimientoResponseDTO {
    private Long movimientoId;
    private Date fecha ;
    private TipoMovimiento tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldoInicial;
    private Long cuentaId;
}
