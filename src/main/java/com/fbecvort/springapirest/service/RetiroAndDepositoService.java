package com.fbecvort.springapirest.service;

import com.fbecvort.springapirest.dto.retiroanddeposito.RetiroAndDepositoResponseDTO;
import com.fbecvort.springapirest.enumeration.TipoMovimiento;

import java.math.BigDecimal;

public interface RetiroAndDepositoService {
    RetiroAndDepositoResponseDTO realizarMovimiento(Long cuentaId, BigDecimal valor, TipoMovimiento tipoMovimiento);
}
