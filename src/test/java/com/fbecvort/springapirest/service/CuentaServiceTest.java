package com.fbecvort.springapirest.service;

import com.fbecvort.springapirest.dto.cuenta.CuentaRequestDTO;
import com.fbecvort.springapirest.dto.cuenta.CuentaResponseDTO;
import com.fbecvort.springapirest.entity.Cliente;
import com.fbecvort.springapirest.entity.Cuenta;
import com.fbecvort.springapirest.entity.Movimiento;
import com.fbecvort.springapirest.enumeration.TipoCuenta;
import com.fbecvort.springapirest.exception.crud.EntityWithAssociatedElementsException;
import com.fbecvort.springapirest.exception.crud.NoSuchElementException;
import com.fbecvort.springapirest.repository.ClienteRepository;
import com.fbecvort.springapirest.repository.CuentaRepository;
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
class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    @Test
    void save() {
        // Given
        CuentaRequestDTO request = CuentaRequestDTO
                .builder()
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.AHORROS)
                .saldo(new BigDecimal("544.99"))
                .estado(true)
                .clienteId(1L)
                .build();

        Cliente cliente = Cliente.builder().personaId(1L).build();

        Cuenta savedCuenta = Cuenta
                .builder()
                .cuentaId(1L)
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.AHORROS)
                .saldo(new BigDecimal("544.99"))
                .estado(true)
                .cliente(cliente)
                .build();

        when(cuentaRepository.save(any(Cuenta.class))).then(invocationOnMock -> {
            Cuenta c = invocationOnMock.getArgument(0);
            c.setCuentaId(1L);
            return c;
        });

        when(clienteRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(cliente));

        // When
        CuentaResponseDTO response = cuentaService.save(request);

        // Then
        assertEquals(savedCuenta.getCuentaId(),                 response.getCuentaId());
        assertEquals(savedCuenta.getNumeroCuenta(),             response.getNumeroCuenta());
        assertEquals(savedCuenta.getTipoCuenta(),               response.getTipoCuenta());
        assertEquals(savedCuenta.getSaldo(),                    response.getSaldo());
        assertEquals(savedCuenta.isEstado(),                    response.isEstado());
        assertEquals(savedCuenta.getCliente().getPersonaId(),   response.getClienteId());
    }

    @Test
    void findAll() {
        // Given
        int page = Integer.parseInt(PaginationUtils.PAGE_DEFAULT_VALUE);
        int size = Integer.parseInt(PaginationUtils.SIZE_DEFAULT_VALUE);
        String sortBy = PaginationUtils.CUENTA_SORT_BY_DEFAULT_VALUE;
        String sortOrder = PaginationUtils.SORT_ORDER_DEFAULT_VALUE;

        Cliente cliente = Cliente.builder().personaId(1L).build();

        List<Cuenta> cuentaList = Arrays.asList(
                Cuenta
                        .builder()
                        .cuentaId(1L)
                        .numeroCuenta(444555)
                        .tipoCuenta(TipoCuenta.AHORROS)
                        .saldo(new BigDecimal("544.99"))
                        .estado(true)
                        .cliente(cliente)
                        .build(),

                Cuenta
                        .builder()
                        .cuentaId(2L)
                        .numeroCuenta(123123)
                        .tipoCuenta(TipoCuenta.CORRIENTE)
                        .saldo(new BigDecimal("123.99"))
                        .estado(true)
                        .cliente(cliente)
                        .build()
        );

        Page<Cuenta> cuentaPage = new PageImpl<>(cuentaList);

        when(cuentaRepository.findAll(any(Pageable.class))).thenReturn(cuentaPage);

        // When
        Page<CuentaResponseDTO> response = cuentaService.findAll(page, size, sortBy, sortOrder);

        // Then
        assertEquals(cuentaList.size(), response.getContent().size());

        assertEquals(cuentaList.get(0).getCuentaId(),               response.getContent().get(0).getCuentaId());
        assertEquals(cuentaList.get(0).getNumeroCuenta(),           response.getContent().get(0).getNumeroCuenta());
        assertEquals(cuentaList.get(0).getTipoCuenta(),             response.getContent().get(0).getTipoCuenta());
        assertEquals(cuentaList.get(0).getSaldo(),                  response.getContent().get(0).getSaldo());
        assertEquals(cuentaList.get(0).isEstado(),                  response.getContent().get(0).isEstado());
        assertEquals(cuentaList.get(0).getCliente().getPersonaId(), response.getContent().get(0).getClienteId());

        assertEquals(cuentaList.get(1).getCuentaId(),               response.getContent().get(1).getCuentaId());
        assertEquals(cuentaList.get(1).getNumeroCuenta(),           response.getContent().get(1).getNumeroCuenta());
        assertEquals(cuentaList.get(1).getTipoCuenta(),             response.getContent().get(1).getTipoCuenta());
        assertEquals(cuentaList.get(1).getSaldo(),                  response.getContent().get(1).getSaldo());
        assertEquals(cuentaList.get(1).isEstado(),                  response.getContent().get(1).isEstado());
        assertEquals(cuentaList.get(1).getCliente().getPersonaId(), response.getContent().get(1).getClienteId());
    }

    @Test
    void findById() {
        // Given
        Long id = 1L;

        Cliente cliente = Cliente.builder().personaId(1L).build();

        Cuenta cuenta = Cuenta
                .builder()
                .cuentaId(1L)
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.AHORROS)
                .saldo(new BigDecimal("544.99"))
                .estado(true)
                .cliente(cliente)
                .build();

        CuentaResponseDTO expectedResponse = CuentaResponseDTO
                .builder()
                .cuentaId(1L)
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.AHORROS)
                .saldo(new BigDecimal("544.99"))
                .estado(true)
                .clienteId(cliente.getPersonaId())
                .build();

        when(cuentaRepository.findById(any(Long.class))).thenReturn(Optional.of(cuenta));

        // When
        CuentaResponseDTO response = cuentaService.findById(id);

        // Then
        assertEquals(expectedResponse.getCuentaId(),        response.getCuentaId());
        assertEquals(expectedResponse.getNumeroCuenta(),    response.getNumeroCuenta());
        assertEquals(expectedResponse.getTipoCuenta(),      response.getTipoCuenta());
        assertEquals(expectedResponse.getSaldo(),           response.getSaldo());
        assertEquals(expectedResponse.isEstado(),           response.isEstado());
        assertEquals(expectedResponse.getClienteId(),       response.getClienteId());
    }

    @Test
    public void findById_withInvalidId(){
        // Given
        Long id = -1L;

        when(cuentaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> cuentaService.findById(id));
    }

    @Test
    void update_withSameClienteId() {
        // Given
        Long cuentaId = 1L;
        Long clienteId = 2L;

        Cliente cliente = Cliente.builder().personaId(clienteId).build();

        CuentaRequestDTO request = CuentaRequestDTO
                .builder()
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.CORRIENTE)
                .saldo(new BigDecimal("899.99"))
                .estado(true)
                .clienteId(clienteId)
                .build();

        Cuenta currentCuenta = Cuenta
                .builder()
                .cuentaId(cuentaId)
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.AHORROS)
                .saldo(new BigDecimal("549.99"))
                .estado(true)
                .cliente(cliente)
                .build();

        Cuenta updatedCuenta = Cuenta
                .builder()
                .cuentaId(cuentaId)
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.CORRIENTE)
                .saldo(new BigDecimal("899.99"))
                .estado(true)
                .cliente(cliente)
                .build();

        CuentaResponseDTO expectedResponse = CuentaResponseDTO
                .builder()
                .cuentaId(cuentaId)
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.CORRIENTE)
                .saldo(new BigDecimal("899.99"))
                .estado(true)
                .clienteId(clienteId)
                .build();

        when(cuentaRepository.findById(any(Long.class))).thenReturn(Optional.of(currentCuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(updatedCuenta);

        // When
        CuentaResponseDTO response = cuentaService.update(cuentaId, request);

        // Then
        assertEquals(expectedResponse.getCuentaId(),        response.getCuentaId());
        assertEquals(expectedResponse.getNumeroCuenta(),    response.getNumeroCuenta());
        assertEquals(expectedResponse.getTipoCuenta(),      response.getTipoCuenta());
        assertEquals(expectedResponse.getSaldo(),           response.getSaldo());
        assertEquals(expectedResponse.isEstado(),           response.isEstado());
        assertEquals(expectedResponse.getClienteId(),       response.getClienteId());
    }

    @Test
    void update_withNewClienteId(){
        // Given
        Long cuentaId = 1L;
        Long clienteActualId = 2L;
        Long clienteNuevoId = 3L;

        Cliente clienteActual = Cliente.builder().personaId(clienteActualId).build();
        Cliente clienteNuevo = Cliente.builder().personaId(clienteNuevoId).build();

        CuentaRequestDTO request = CuentaRequestDTO
                .builder()
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.CORRIENTE)
                .saldo(new BigDecimal("899.99"))
                .estado(true)
                .clienteId(clienteNuevoId)
                .build();

        Cuenta currentCuenta = Cuenta
                .builder()
                .cuentaId(cuentaId)
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.AHORROS)
                .saldo(new BigDecimal("549.99"))
                .estado(true)
                .cliente(clienteActual)
                .build();

        Cuenta updatedCuenta = Cuenta
                .builder()
                .cuentaId(cuentaId)
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.CORRIENTE)
                .saldo(new BigDecimal("899.99"))
                .estado(true)
                .cliente(clienteNuevo)
                .build();

        CuentaResponseDTO expectedResponse = CuentaResponseDTO
                .builder()
                .cuentaId(cuentaId)
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.CORRIENTE)
                .saldo(new BigDecimal("899.99"))
                .estado(true)
                .clienteId(clienteNuevoId)
                .build();

        when(cuentaRepository.findById(any(Long.class))).thenReturn(Optional.of(currentCuenta));
        when(clienteRepository.findById(any(Long.class))).thenReturn(Optional.of(clienteNuevo));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(updatedCuenta);

        // When
        CuentaResponseDTO response = cuentaService.update(cuentaId, request);

        // Then
        assertEquals(expectedResponse.getCuentaId(),        response.getCuentaId());
        assertEquals(expectedResponse.getNumeroCuenta(),    response.getNumeroCuenta());
        assertEquals(expectedResponse.getTipoCuenta(),      response.getTipoCuenta());
        assertEquals(expectedResponse.getSaldo(),           response.getSaldo());
        assertEquals(expectedResponse.isEstado(),           response.isEstado());
        assertEquals(expectedResponse.getClienteId(),       response.getClienteId());
    }

    @Test
    void update_withInvalidClienteId(){
        // Given
        Long cuentaId = 1L;
        Long clienteActualId = 2L;
        Long clienteInvalidoId = -3L;

        Cliente clienteActual = Cliente.builder().personaId(clienteActualId).build();

        CuentaRequestDTO request = CuentaRequestDTO
                .builder()
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.CORRIENTE)
                .saldo(new BigDecimal("899.99"))
                .estado(true)
                .clienteId(clienteInvalidoId)
                .build();

        Cuenta currentCuenta = Cuenta
                .builder()
                .cuentaId(cuentaId)
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.AHORROS)
                .saldo(new BigDecimal("549.99"))
                .estado(true)
                .cliente(clienteActual)
                .build();

        when(cuentaRepository.findById(any(Long.class))).thenReturn(Optional.of(currentCuenta));
        when(clienteRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> cuentaService.update(cuentaId, request));
    }

    @Test
    void update_withInvalidCuentaId(){
        // Given
        Long cuentaInvalidaId = -1L;
        Long clienteId = 2L;

        CuentaRequestDTO request = CuentaRequestDTO
                .builder()
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.CORRIENTE)
                .saldo(new BigDecimal("899.99"))
                .estado(true)
                .clienteId(clienteId)
                .build();

        when(cuentaRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> cuentaService.update(cuentaInvalidaId, request));
    }

    @Test
    void deleteById() {
        // Given
        Long id = 1L;

        Cliente cliente = Cliente.builder().personaId(1L).build();

        Cuenta cuenta = Cuenta
                .builder()
                .cuentaId(1L)
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.AHORROS)
                .saldo(new BigDecimal("544.99"))
                .estado(true)
                .cliente(cliente)
                .build();

        when(cuentaRepository.findById(id)).thenReturn(Optional.of(cuenta));
        doNothing().when(cuentaRepository).deleteById(id);

        // When
        cuentaService.deleteById(id);

        // Then
        verify(cuentaRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_withMovimientos() {
        // Given
        Long id = 1L;

        Cliente cliente = Cliente.builder().personaId(1L).build();

        Cuenta cuenta = Cuenta
                .builder()
                .cuentaId(1L)
                .numeroCuenta(444555)
                .tipoCuenta(TipoCuenta.AHORROS)
                .saldo(new BigDecimal("544.99"))
                .estado(true)
                .cliente(cliente)
                .build();

        cuenta.getMovimientos().add(new Movimiento());
        when(cuentaRepository.findById(id)).thenReturn(Optional.of(cuenta));

        // When & Then
        assertThrows(EntityWithAssociatedElementsException.class, () -> cuentaService.deleteById(id));
        verify(cuentaRepository, never()).deleteById(id);
    }

    @Test
    void deleteById_withInvalidId() {
        // Given
        Long id = -1L;

        when(cuentaRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> cuentaService.deleteById(id));
        verify(cuentaRepository, never()).deleteById(id);
    }

}