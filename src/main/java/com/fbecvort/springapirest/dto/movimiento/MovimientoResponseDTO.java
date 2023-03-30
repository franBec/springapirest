package com.fbecvort.springapirest.dto.movimiento;

import com.fbecvort.springapirest.enumeration.TipoMovimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MovimientoResponseDTO {
    private Long movimientoId;
    private Date fecha;
    private TipoMovimiento tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldoInicial;
    private Long cuentaId;
}
