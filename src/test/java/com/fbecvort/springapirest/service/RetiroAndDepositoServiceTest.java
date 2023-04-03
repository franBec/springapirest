package com.fbecvort.springapirest.service;

import com.fbecvort.springapirest.dto.retiroanddeposito.RetiroAndDepositoResponseDTO;
import com.fbecvort.springapirest.entity.Cuenta;
import com.fbecvort.springapirest.entity.Movimiento;
import com.fbecvort.springapirest.enumeration.TipoMovimiento;
import com.fbecvort.springapirest.exception.bussinessneed.CupoDiarioExcedidoException;
import com.fbecvort.springapirest.exception.bussinessneed.SaldoNoDisponibleException;
import com.fbecvort.springapirest.exception.crud.NoSuchElementException;
import com.fbecvort.springapirest.repository.CuentaRepository;
import com.fbecvort.springapirest.repository.MovimientoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RetiroAndDepositoServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @InjectMocks
    private RetiroAndDepositoServiceImpl retiroAndDepositoService;

    @Test
    void realizarMovimiento_cuentaInvalida() {
        // Given
        Long cuentaId = -1L;
        BigDecimal valor = BigDecimal.valueOf(600);
        TipoMovimiento tipoMovimiento = TipoMovimiento.RETIRO;

        when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> retiroAndDepositoService.realizarMovimiento(cuentaId, valor, tipoMovimiento));
    }

    @Test
    void realizarMovimiento_deposito() {
        // Given
        Long cuentaId = 1L;
        Long movimientoId = 10L;

        BigDecimal valor = BigDecimal.valueOf(100);
        TipoMovimiento tipoMovimiento = TipoMovimiento.DEPOSITO;

        Cuenta cuenta = Cuenta.builder()
                .cuentaId(cuentaId)
                .saldo(BigDecimal.valueOf(500))
                .build();

        Movimiento movimiento = Movimiento.builder().movimientoId(movimientoId).build();

        when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);

        // When
        RetiroAndDepositoResponseDTO response = retiroAndDepositoService.realizarMovimiento(cuentaId, valor, tipoMovimiento);

        // Then
        assertNotNull(response);
        assertNotNull(response.getMessage());

        assertEquals(cuentaId,                  response.getCuentaId());
        assertEquals(movimientoId,              response.getMovimientoId());
        assertEquals(BigDecimal.valueOf(600),   cuenta.getSaldo());
    }

    @Test
    void realizarMovimiento_retiro() throws NoSuchFieldException, IllegalAccessException {

        //This make the private BigDecimal retiroLimiteDiario in RetiroAndDepositoServiceImpl not be null
        Field field = retiroAndDepositoService.getClass().getDeclaredField("retiroLimiteDiario");
        field.setAccessible(true);
        field.set(retiroAndDepositoService, new BigDecimal("1000.0"));

        // Given
        Long cuentaId = 1L;
        Long movimientoId = 10L;

        BigDecimal valor = BigDecimal.valueOf(100);
        TipoMovimiento tipoMovimiento = TipoMovimiento.RETIRO;

        Cuenta cuenta = Cuenta.builder()
                .cuentaId(cuentaId)
                .saldo(BigDecimal.valueOf(500))
                .build();

        Movimiento movimiento = Movimiento.builder().movimientoId(movimientoId).build();

        when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);

        // When
        RetiroAndDepositoResponseDTO response = retiroAndDepositoService.realizarMovimiento(cuentaId, valor, tipoMovimiento);

        // Then
        assertNotNull(response);
        assertNotNull(response.getMessage());

        assertEquals(cuentaId,                  response.getCuentaId());
        assertEquals(movimientoId,              response.getMovimientoId());
        assertEquals(BigDecimal.valueOf(400),   cuenta.getSaldo());
    }

    @Test
    void realizarMovimiento_retiroSaldoNoDisponible() {
        // Given
        Long cuentaId = 1L;
        BigDecimal valor = BigDecimal.valueOf(501);
        TipoMovimiento tipoMovimiento = TipoMovimiento.RETIRO;

        Cuenta cuenta = Cuenta.builder()
                .cuentaId(cuentaId)
                .saldo(BigDecimal.valueOf(500))
                .build();

        when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.of(cuenta));

        // When & Then
        assertThrows(SaldoNoDisponibleException.class, () -> retiroAndDepositoService.realizarMovimiento(cuentaId, valor, tipoMovimiento));
    }

    @Test
    void realizarMovimiento_retiroCupoDiarioExcedido() throws NoSuchFieldException, IllegalAccessException {

        //This make the private BigDecimal retiroLimiteDiario in RetiroAndDepositoServiceImpl not be null
        Field field = retiroAndDepositoService.getClass().getDeclaredField("retiroLimiteDiario");
        field.setAccessible(true);
        field.set(retiroAndDepositoService, new BigDecimal("1000.0"));

        Long cuentaId = 1L;
        BigDecimal valor = BigDecimal.valueOf(200);
        TipoMovimiento tipoMovimiento = TipoMovimiento.RETIRO;

        Cuenta cuenta = Cuenta.builder()
                .cuentaId(cuentaId)
                .saldo(new BigDecimal("5000.0"))
                .build();

        cuenta.getMovimientos().addAll(Arrays.asList(
                Movimiento
                        .builder()
                        .movimientoId(10L)
                        .fecha(new Date())
                        .tipoMovimiento(TipoMovimiento.RETIRO)
                        .valor(new BigDecimal("500.0"))
                        .saldoInicial(new BigDecimal("5900.0"))
                        .cuenta(cuenta)
                        .build(),

                Movimiento
                        .builder()
                        .movimientoId(11L)
                        .fecha(new Date())
                        .tipoMovimiento(TipoMovimiento.RETIRO)
                        .valor(new BigDecimal("400.0"))
                        .saldoInicial(new BigDecimal("5400.0"))
                        .cuenta(cuenta)
                        .build()
        ));

        when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.of(cuenta));

        // When & Then
        assertThrows(CupoDiarioExcedidoException.class, () -> retiroAndDepositoService.realizarMovimiento(cuentaId, valor, tipoMovimiento));

    }
}