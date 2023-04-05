package com.fbecvort.springapirest.service;

import com.fbecvort.springapirest.dto.movimiento.MovimientoRequestDTO;
import com.fbecvort.springapirest.dto.movimiento.MovimientoResponseDTO;
import com.fbecvort.springapirest.entity.Cuenta;
import com.fbecvort.springapirest.entity.Movimiento;
import com.fbecvort.springapirest.enumeration.TipoMovimiento;
import com.fbecvort.springapirest.exception.crud.NoSuchElementException;
import com.fbecvort.springapirest.repository.CuentaRepository;
import com.fbecvort.springapirest.repository.MovimientoRepository;
import com.fbecvort.springapirest.util.DateUtils;
import com.fbecvort.springapirest.util.PaginationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private MovimientoServiceImpl movimientoService;

    @Test
    void save() throws ParseException {
        // Given
        Long movimientoId = 1L;
        Long cuentaId = 10L;

        MovimientoRequestDTO request = MovimientoRequestDTO
                .builder()
                .fecha(DateUtils.createDateFromString("2023-03-29T12:00:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(100.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuentaId(cuentaId)
                .build();

        Cuenta cuenta = Cuenta.builder().cuentaId(cuentaId).build();

        Movimiento savedMovimiento = Movimiento
                .builder()
                .movimientoId(movimientoId)
                .fecha(DateUtils.createDateFromString("2023-03-29T12:00:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(100.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuenta(cuenta)
                .build();

        when(movimientoRepository.save(any(Movimiento.class))).then(invocationOnMock -> {
           Movimiento m = invocationOnMock.getArgument(0);
           m.setMovimientoId(movimientoId);
           return m;
        });

        when(cuentaRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(cuenta));

        // When
        MovimientoResponseDTO response = movimientoService.save(request);

        // Then
        assertEquals(savedMovimiento.getMovimientoId(),          response.getMovimientoId());
        assertEquals(savedMovimiento.getFecha(),                 response.getFecha());
        assertEquals(savedMovimiento.getTipoMovimiento(),        response.getTipoMovimiento());
        assertEquals(savedMovimiento.getValor(),                 response.getValor());
        assertEquals(savedMovimiento.getSaldoInicial(),          response.getSaldoInicial());
        assertEquals(savedMovimiento.getCuenta().getCuentaId(),  response.getCuentaId());
    }

    @Test
    void findAll() throws ParseException {
        // Given
        int page = Integer.parseInt(PaginationUtils.PAGE_DEFAULT_VALUE);
        int size = Integer.parseInt(PaginationUtils.SIZE_DEFAULT_VALUE);
        String sortBy = PaginationUtils.CUENTA_SORT_BY_DEFAULT_VALUE;
        String sortOrder = PaginationUtils.SORT_ORDER_DEFAULT_VALUE;

        Cuenta cuenta = Cuenta.builder().cuentaId(10L).build();

        Movimiento movimiento = Movimiento
                .builder()
                .movimientoId(1L)
                .fecha(DateUtils.createDateFromString("2023-03-29T12:00:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(100.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuenta(cuenta)
                .build();

        Movimiento anotherMovimiento = Movimiento
                .builder()
                .movimientoId(2L)
                .fecha(DateUtils.createDateFromString("2023-04-01T10:00:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.DEPOSITO)
                .valor(BigDecimal.valueOf(500.0))
                .saldoInicial(BigDecimal.valueOf(99.0))
                .cuenta(cuenta)
                .build();

        List<Movimiento> movimientoList = Arrays.asList(movimiento, anotherMovimiento);
        Page<Movimiento> movimientoPage = new PageImpl<>(movimientoList);

        when(movimientoRepository.findAll(any(Pageable.class))).thenReturn(movimientoPage);

        // When
        Page<MovimientoResponseDTO> response = movimientoService.findAll(page, size, sortBy, sortOrder);

        // Then
        assertEquals(movimientoList.size(), response.getContent().size());

        assertEquals(movimientoList.get(0).getMovimientoId(),           response.getContent().get(0).getMovimientoId());
        assertEquals(movimientoList.get(0).getFecha(),                  response.getContent().get(0).getFecha());
        assertEquals(movimientoList.get(0).getTipoMovimiento(),         response.getContent().get(0).getTipoMovimiento());
        assertEquals(movimientoList.get(0).getValor(),                  response.getContent().get(0).getValor());
        assertEquals(movimientoList.get(0).getSaldoInicial(),           response.getContent().get(0).getSaldoInicial());
        assertEquals(movimientoList.get(0).getCuenta().getCuentaId(),   response.getContent().get(0).getCuentaId());

        assertEquals(movimientoList.get(1).getMovimientoId(),           response.getContent().get(1).getMovimientoId());
        assertEquals(movimientoList.get(1).getFecha(),                  response.getContent().get(1).getFecha());
        assertEquals(movimientoList.get(1).getTipoMovimiento(),         response.getContent().get(1).getTipoMovimiento());
        assertEquals(movimientoList.get(1).getValor(),                  response.getContent().get(1).getValor());
        assertEquals(movimientoList.get(1).getSaldoInicial(),           response.getContent().get(1).getSaldoInicial());
        assertEquals(movimientoList.get(1).getCuenta().getCuentaId(),   response.getContent().get(1).getCuentaId());
    }

    @Test
    void findById() throws ParseException {
        // Given
        Long id = 1L;
        Long cuentaId = 10L;

        Cuenta cuenta = Cuenta.builder().cuentaId(cuentaId).build();

        Movimiento movimiento = Movimiento
                .builder()
                .movimientoId(1L)
                .fecha(DateUtils.createDateFromString("2023-03-29T12:00:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(100.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuenta(cuenta)
                .build();

        MovimientoResponseDTO expectedResponse = MovimientoResponseDTO
                .builder()
                .movimientoId(1L)
                .fecha(DateUtils.createDateFromString("2023-03-29T12:00:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(100.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuentaId(cuentaId)
                .build();

        when(movimientoRepository.findById(any(Long.class))).thenReturn(Optional.of(movimiento));

        // When
        MovimientoResponseDTO response = movimientoService.findById(id);

        // Then
        assertEquals(expectedResponse.getMovimientoId(),    response.getMovimientoId());
        assertEquals(expectedResponse.getFecha(),           response.getFecha());
        assertEquals(expectedResponse.getTipoMovimiento(),  response.getTipoMovimiento());
        assertEquals(expectedResponse.getValor(),           response.getValor());
        assertEquals(expectedResponse.getSaldoInicial(),    response.getSaldoInicial());
        assertEquals(expectedResponse.getCuentaId(),        response.getCuentaId());
    }

    @Test
    void findById_withInvalidId(){
        // Given
        Long id = -1L;
        when(movimientoRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> movimientoService.findById(id));
    }

    @Test
    void update_withSameCuentaId() throws ParseException {
        Long movimientoId = 1L;
        Long cuentaId = 10L;

        Cuenta cuenta = Cuenta.builder().cuentaId(cuentaId).build();

        MovimientoRequestDTO request = MovimientoRequestDTO
                .builder()
                .fecha(DateUtils.createDateFromString("2023-03-29T12:15:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(10.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuentaId(cuentaId)
                .build();

        Movimiento currentMovimiento = Movimiento
                .builder()
                .movimientoId(1L)
                .fecha(DateUtils.createDateFromString("2023-03-29T12:00:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(100.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuenta(cuenta)
                .build();

        Movimiento updatedMovimiento = Movimiento
                .builder()
                .movimientoId(1L)
                .fecha(DateUtils.createDateFromString("2023-03-29T12:15:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(10.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuenta(cuenta)
                .build();

        MovimientoResponseDTO expectedResponse = MovimientoResponseDTO
                .builder()
                .movimientoId(1L)
                .fecha(DateUtils.createDateFromString("2023-03-29T12:15:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(10.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuentaId(cuentaId)
                .build();

        when(movimientoRepository.findById(any(Long.class))).thenReturn(Optional.of(currentMovimiento));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(updatedMovimiento);

        // When
        MovimientoResponseDTO response = movimientoService.update(movimientoId, request);

        // Then
        assertEquals(expectedResponse.getMovimientoId(),    response.getMovimientoId());
        assertEquals(expectedResponse.getFecha(),           response.getFecha());
        assertEquals(expectedResponse.getTipoMovimiento(),  response.getTipoMovimiento());
        assertEquals(expectedResponse.getValor(),           response.getValor());
        assertEquals(expectedResponse.getSaldoInicial(),    response.getSaldoInicial());
        assertEquals(expectedResponse.getCuentaId(),        response.getCuentaId());
    }

    @Test
    void update_withNewCuentaId() throws ParseException {
        Long movimientoId = 1L;
        Long cuentaActualId = 10L;
        Long cuentaNuevaId = 11L;

        Cuenta cuentaActual = Cuenta.builder().cuentaId(cuentaActualId).build();
        Cuenta cuentaNueva = Cuenta.builder().cuentaId(cuentaNuevaId).build();

        MovimientoRequestDTO request = MovimientoRequestDTO
                .builder()
                .fecha(DateUtils.createDateFromString("2023-03-29T12:15:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(10.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuentaId(cuentaNuevaId)
                .build();

        Movimiento currentMovimiento = Movimiento
                .builder()
                .movimientoId(1L)
                .fecha(DateUtils.createDateFromString("2023-03-29T12:00:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(100.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuenta(cuentaActual)
                .build();

        Movimiento updatedMovimiento = Movimiento
                .builder()
                .movimientoId(1L)
                .fecha(DateUtils.createDateFromString("2023-03-29T12:15:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(10.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuenta(cuentaNueva)
                .build();

        MovimientoResponseDTO expectedResponse = MovimientoResponseDTO
                .builder()
                .movimientoId(1L)
                .fecha(DateUtils.createDateFromString("2023-03-29T12:15:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(10.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuentaId(cuentaNuevaId)
                .build();

        when(movimientoRepository.findById(any(Long.class))).thenReturn(Optional.of(currentMovimiento));
        when(cuentaRepository.findById(any(Long.class))).thenReturn(Optional.of(cuentaNueva));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(updatedMovimiento);

        // When
        MovimientoResponseDTO response = movimientoService.update(movimientoId, request);

        // Then
        assertEquals(expectedResponse.getMovimientoId(),    response.getMovimientoId());
        assertEquals(expectedResponse.getFecha(),           response.getFecha());
        assertEquals(expectedResponse.getTipoMovimiento(),  response.getTipoMovimiento());
        assertEquals(expectedResponse.getValor(),           response.getValor());
        assertEquals(expectedResponse.getSaldoInicial(),    response.getSaldoInicial());
        assertEquals(expectedResponse.getCuentaId(),        response.getCuentaId());

    }

    @Test
    void update_withInvalidCuentaId() throws ParseException {
        // Given
        Long movimientoId = 1L;
        Long cuentaActualId = 10L;
        Long cuentaInvalidaId = -1L;

        Cuenta cuentaActual = Cuenta.builder().cuentaId(cuentaActualId).build();

        MovimientoRequestDTO request = MovimientoRequestDTO
                .builder()
                .fecha(DateUtils.createDateFromString("2023-03-29T12:15:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(10.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuentaId(cuentaInvalidaId)
                .build();

        Movimiento currentMovimiento = Movimiento
                .builder()
                .movimientoId(1L)
                .fecha(DateUtils.createDateFromString("2023-03-29T12:00:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(100.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuenta(cuentaActual)
                .build();

        when(movimientoRepository.findById(any(Long.class))).thenReturn(Optional.of(currentMovimiento));
        when(cuentaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> movimientoService.update(movimientoId, request));
    }

    @Test
    void update_withInvalidMovimientoId() throws ParseException {
        Long movimientoInvalidoId = -1L;
        Long cuentaId = 10L;

        MovimientoRequestDTO request = MovimientoRequestDTO
                .builder()
                .fecha(DateUtils.createDateFromString("2023-03-29T12:15:00.000-03:00"))
                .tipoMovimiento(TipoMovimiento.RETIRO)
                .valor(BigDecimal.valueOf(10.0))
                .saldoInicial(BigDecimal.valueOf(50.0))
                .cuentaId(cuentaId)
                .build();

        when(movimientoRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> movimientoService.update(movimientoInvalidoId, request));
    }

    @Test
    void deleteById() {
        Long id = 1L;

        when(movimientoRepository.existsById(id)).thenReturn(true);
        doNothing().when(movimientoRepository).deleteById(id);

        // When
        movimientoService.deleteById(id);

        // Then
        verify(movimientoRepository,times(1)).deleteById(id);
    }

    @Test
    void deleteById_withInvalidId() {
        //Given
        Long id = -1L;
        when(movimientoRepository.existsById(id)).thenReturn(false);

        // When & Then
        assertThrows(NoSuchElementException.class, () ->movimientoService.deleteById(id));
        verify(cuentaRepository, never()).deleteById(id);
    }
}