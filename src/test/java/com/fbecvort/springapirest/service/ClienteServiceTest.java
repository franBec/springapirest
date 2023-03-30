package com.fbecvort.springapirest.service;

import com.fbecvort.springapirest.dto.cliente.ClienteRequestDTO;
import com.fbecvort.springapirest.dto.cliente.ClienteResponseDTO;
import com.fbecvort.springapirest.entity.Cliente;
import com.fbecvort.springapirest.entity.Cuenta;
import com.fbecvort.springapirest.enumeration.Genero;
import com.fbecvort.springapirest.exception.crud.EntityWithAssociatedElementsException;
import com.fbecvort.springapirest.exception.crud.NoSuchElementException;
import com.fbecvort.springapirest.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    public void testSave() {
        // Given
        ClienteRequestDTO request = ClienteRequestDTO
                .builder()
                .nombre("Franco Becvort")
                .genero(Genero.MASCULINO)
                .edad(23)
                .identificacion("41809105")
                .direccion("Rivadavia 1470")
                .telefono("234567890")
                .contrasena("password")
                .estado(true)
                .build();

        Cliente savedCliente = Cliente
                .builder()
                .personaId(1L)
                .nombre("Franco Becvort")
                .genero(Genero.MASCULINO)
                .edad(23)
                .identificacion("41809105")
                .direccion("Rivadavia 1470")
                .telefono("234567890")
                .contrasena("password")
                .estado(true)
                .build();

        when(clienteRepository.save(any(Cliente.class))).then(invocationOnMock -> {
            Cliente cliente = invocationOnMock.getArgument(0);
            cliente.setPersonaId(1L);
            return cliente;
        });

        // When
        ClienteResponseDTO response = clienteService.save(request);

        // Then
        assertEquals(savedCliente.getPersonaId(),       response.getPersonaId());
        assertEquals(savedCliente.getNombre(),          response.getNombre());
        assertEquals(savedCliente.getGenero(),          response.getGenero());
        assertEquals(savedCliente.getEdad(),            response.getEdad());
        assertEquals(savedCliente.getIdentificacion(),  response.getIdentificacion());
        assertEquals(savedCliente.getDireccion(),       response.getDireccion());
        assertEquals(savedCliente.getTelefono(),        response.getTelefono());
        assertEquals(savedCliente.getContrasena(),      response.getContrasena());
        assertEquals(savedCliente.isEstado(),           response.isEstado());
    }

    @Test
    void findAll() {
        // Given
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String sortOrder = "asc";

        List<Cliente> clienteList = Arrays.asList(
                Cliente
                        .builder()
                        .personaId(1L)
                        .nombre("Franco Becvort")
                        .genero(Genero.MASCULINO)
                        .edad(23)
                        .identificacion("41809105")
                        .direccion("Rivadavia 1470")
                        .telefono("234567890")
                        .contrasena("password")
                        .estado(true)
                        .build(),

                Cliente
                        .builder()
                        .personaId(1L)
                        .nombre("Jenny Becvort")
                        .genero(Genero.FEMENINO)
                        .edad(22)
                        .identificacion("44123456")
                        .direccion("Los Andes 1665")
                        .telefono("987654321")
                        .contrasena("contrasena")
                        .estado(true)
                        .build()
        );

        Page<Cliente> clientesPage = new PageImpl<>(clienteList);

        when(clienteRepository.findAll(any(Pageable.class))).thenReturn(clientesPage);

        // When
        Page<ClienteResponseDTO> response = clienteService.findAll(page, size, sortBy, sortOrder);

        // Then
        assertEquals(clienteList.size(), response.getContent().size());

        assertEquals(clienteList.get(0).getPersonaId(),         response.getContent().get(0).getPersonaId());
        assertEquals(clienteList.get(0).getNombre(),            response.getContent().get(0).getNombre());
        assertEquals(clienteList.get(0).getGenero(),            response.getContent().get(0).getGenero());
        assertEquals(clienteList.get(0).getEdad(),              response.getContent().get(0).getEdad());
        assertEquals(clienteList.get(0).getIdentificacion(),    response.getContent().get(0).getIdentificacion());
        assertEquals(clienteList.get(0).getDireccion(),         response.getContent().get(0).getDireccion());
        assertEquals(clienteList.get(0).getTelefono(),          response.getContent().get(0).getTelefono());
        assertEquals(clienteList.get(0).getContrasena(),        response.getContent().get(0).getContrasena());
        assertEquals(clienteList.get(0).isEstado(),             response.getContent().get(0).isEstado());

        assertEquals(clienteList.get(1).getPersonaId(),         response.getContent().get(1).getPersonaId());
        assertEquals(clienteList.get(1).getNombre(),            response.getContent().get(1).getNombre());
        assertEquals(clienteList.get(1).getGenero(),            response.getContent().get(1).getGenero());
        assertEquals(clienteList.get(1).getEdad(),              response.getContent().get(1).getEdad());
        assertEquals(clienteList.get(1).getIdentificacion(),    response.getContent().get(1).getIdentificacion());
        assertEquals(clienteList.get(1).getDireccion(),         response.getContent().get(1).getDireccion());
        assertEquals(clienteList.get(1).getTelefono(),          response.getContent().get(1).getTelefono());
        assertEquals(clienteList.get(1).getContrasena(),        response.getContent().get(1).getContrasena());
        assertEquals(clienteList.get(1).isEstado(),             response.getContent().get(1).isEstado());
    }

    @Test
    void findById() {
        // Given
        Long id = 1L;

        Cliente cliente = Cliente
                .builder()
                .personaId(1L)
                .nombre("Franco Becvort")
                .genero(Genero.MASCULINO)
                .edad(23)
                .identificacion("41809105")
                .direccion("Rivadavia 1470")
                .telefono("234567890")
                .contrasena("password")
                .estado(true)
                .build();

        ClienteResponseDTO expectedResponse = ClienteResponseDTO
                .builder()
                .personaId(1L)
                .nombre("Franco Becvort")
                .genero(Genero.MASCULINO)
                .edad(23)
                .identificacion("41809105")
                .direccion("Rivadavia 1470")
                .telefono("234567890")
                .contrasena("password")
                .estado(true)
                .build();

        when(clienteRepository.findById(any(Long.class))).thenReturn(Optional.of(cliente));

        // When
        ClienteResponseDTO response = clienteService.findById(id);

        // Then
        assertEquals(expectedResponse.getPersonaId(),       response.getPersonaId());
        assertEquals(expectedResponse.getNombre(),          response.getNombre());
        assertEquals(expectedResponse.getGenero(),          response.getGenero());
        assertEquals(expectedResponse.getEdad(),            response.getEdad());
        assertEquals(expectedResponse.getIdentificacion(),  response.getIdentificacion());
        assertEquals(expectedResponse.getDireccion(),       response.getDireccion());
        assertEquals(expectedResponse.getTelefono(),        response.getTelefono());
        assertEquals(expectedResponse.getContrasena(),      response.getContrasena());
        assertEquals(expectedResponse.isEstado(),           response.isEstado());
    }

    @Test
    public void findById_withInvalidId() {
        // Given
        Long id = 1L;

        when(clienteRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> clienteService.findById(id));
    }

    @Test
    void update() {
        // Given
        Long id = 1L;

        ClienteRequestDTO request = ClienteRequestDTO
                .builder()
                .nombre("Francisco Becvort")
                .genero(Genero.MASCULINO)
                .edad(24)
                .identificacion("41809105")
                .direccion("Los Andes 1665")
                .telefono("123456789")
                .contrasena("password123")
                .estado(true)
                .build();

        Cliente currentCliente = Cliente
                .builder()
                .personaId(id)
                .nombre("Franco Becvort")
                .genero(Genero.MASCULINO)
                .edad(23)
                .identificacion("41809105")
                .direccion("Rivadavia 1470")
                .telefono("234567890")
                .contrasena("password")
                .estado(true)
                .build();

        Cliente updatedCliente = Cliente
                .builder()
                .personaId(id)
                .nombre("Francisco Becvort")
                .genero(Genero.MASCULINO)
                .edad(24)
                .identificacion("41809105")
                .direccion("Los Andes 1665")
                .telefono("123456789")
                .contrasena("password123")
                .estado(true)
                .build();

        ClienteResponseDTO expectedResponse = ClienteResponseDTO
                .builder()
                .personaId(id)
                .nombre("Francisco Becvort")
                .genero(Genero.MASCULINO)
                .edad(24)
                .identificacion("41809105")
                .direccion("Los Andes 1665")
                .telefono("123456789")
                .contrasena("password123")
                .estado(true)
                .build();

        when(clienteRepository.findById(any(Long.class))).thenReturn(Optional.of(currentCliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(updatedCliente);

        // When
        ClienteResponseDTO response = clienteService.update(id, request);

        // Then
        assertEquals(expectedResponse.getPersonaId(),       response.getPersonaId());
        assertEquals(expectedResponse.getNombre(),          response.getNombre());
        assertEquals(expectedResponse.getGenero(),          response.getGenero());
        assertEquals(expectedResponse.getEdad(),            response.getEdad());
        assertEquals(expectedResponse.getIdentificacion(),  response.getIdentificacion());
        assertEquals(expectedResponse.getDireccion(),       response.getDireccion());
        assertEquals(expectedResponse.getTelefono(),        response.getTelefono());
        assertEquals(expectedResponse.getContrasena(),      response.getContrasena());
        assertEquals(expectedResponse.isEstado(),           response.isEstado());
    }

    @Test
    public void update_withInvalidId() {
        // Given
        Long id = 1L;

        ClienteRequestDTO request = ClienteRequestDTO
                .builder()
                .nombre("Francisco Becvort")
                .genero(Genero.MASCULINO)
                .edad(24)
                .identificacion("41809105")
                .direccion("Los Andes 1665")
                .telefono("123456789")
                .contrasena("password123")
                .estado(true)
                .build();

        when(clienteRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> clienteService.update(id, request));
    }

    @Test
    void deleteById() {
        // Given
        Long id = 1L;
        Cliente cliente = Cliente
                .builder()
                .personaId(id)
                .nombre("Franco Becvort")
                .genero(Genero.MASCULINO)
                .edad(23)
                .identificacion("41809105")
                .direccion("Rivadavia 1470")
                .telefono("234567890")
                .contrasena("password")
                .estado(true)
                .build();

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).deleteById(id);

        // When
        clienteService.deleteById(id);

        // Then
        verify(clienteRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_withCuentas() {
        // Given
        Long id = 1L;

        Cliente cliente = Cliente
                .builder()
                .personaId(id)
                .nombre("Franco Becvort")
                .genero(Genero.MASCULINO)
                .edad(23)
                .identificacion("41809105")
                .direccion("Rivadavia 1470")
                .telefono("234567890")
                .contrasena("password")
                .estado(true)
                .build();

        Cuenta cuenta = new Cuenta();
        cliente.getCuentas().add(cuenta);
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        // When & Then
        assertThrows(EntityWithAssociatedElementsException.class, () -> clienteService.deleteById(id));
        verify(clienteRepository, never()).deleteById(id);
    }

    @Test
    void deleteById_clientNotFound() {
        // Given
        Long id = 1L;

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> clienteService.deleteById(id));
        verify(clienteRepository, never()).deleteById(id);
    }
}