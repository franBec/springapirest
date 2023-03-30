package com.fbecvort.springapirest.services;

import com.fbecvort.springapirest.dtos.retiroanddeposito.RetiroAndDepositoResponseDTO;
import com.fbecvort.springapirest.enums.TipoMovimiento;

import java.math.BigDecimal;

public interface RetiroAndDepositoService {
    RetiroAndDepositoResponseDTO realizarMovimiento(Long cuentaId, BigDecimal valor, TipoMovimiento tipoMovimiento);
}
