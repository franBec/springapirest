package com.fbecvort.springapirest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbecvort.springapirest.dto.cliente.ClienteRequestDTO;
import com.fbecvort.springapirest.dto.cliente.ClienteResponseDTO;
import com.fbecvort.springapirest.dto.reporte.ReporteCuentaResponseDTO;
import com.fbecvort.springapirest.enumeration.Genero;
import com.fbecvort.springapirest.enumeration.TipoCuenta;
import com.fbecvort.springapirest.exception.crud.EntityWithAssociatedElementsException;
import com.fbecvort.springapirest.exception.crud.IllegalPaginationArgumentException;
import com.fbecvort.springapirest.exception.crud.NoSuchElementException;
import com.fbecvort.springapirest.service.ClienteService;
import com.fbecvort.springapirest.service.ReporteService;
import com.fbecvort.springapirest.util.DateUtils;
import com.fbecvort.springapirest.util.PaginationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {
    final private String url = "/api/clientes";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ReporteService reporteService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void save() throws Exception {
        // Given
        Long id = 1L;

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

        ClienteResponseDTO response = ClienteResponseDTO
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
                .cuentasId(null)
                .build();

        when(clienteService.save(request)).thenReturn(response);

        // When
        mockMvc
                .perform(
                        post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )

        //Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.personaId")      .value(response.getPersonaId()))
                .andExpect(jsonPath("$.nombre")         .value(response.getNombre()))
                .andExpect(jsonPath("$.genero")         .value(response.getGenero().toString()))
                .andExpect(jsonPath("$.edad")           .value(response.getEdad()))
                .andExpect(jsonPath("$.identificacion") .value(response.getIdentificacion()))
                .andExpect(jsonPath("$.direccion")      .value(response.getDireccion()))
                .andExpect(jsonPath("$.telefono")       .value(response.getTelefono()))
                .andExpect(jsonPath("$.contrasena")     .value(response.getContrasena()))
                .andExpect(jsonPath("$.estado")         .value(response.isEstado()))
                .andExpect(jsonPath("$.cuentasId")      .value(response.getCuentasId()));
    }

    @Test
    void findAll_allParams() throws Exception {
        // Given
        int page = Integer.parseInt(PaginationUtils.PAGE_DEFAULT_VALUE);
        int size = Integer.parseInt(PaginationUtils.SIZE_DEFAULT_VALUE);
        String sortBy = PaginationUtils.CLIENTE_SORT_BY_DEFAULT_VALUE;
        String sortOrder = PaginationUtils.SORT_ORDER_DEFAULT_VALUE;

        List<ClienteResponseDTO> content =  Arrays.asList(
                ClienteResponseDTO.builder()
                        .personaId(1L)
                        .build(),
                ClienteResponseDTO.builder()
                        .personaId(2L)
                        .build()
        );

        PageImpl<ClienteResponseDTO> response = new PageImpl<>(
                content,
                PaginationUtils.createPageable(page, size, sortBy, sortOrder),
                content.size()
        );

        when(clienteService.findAll(page, size, sortBy, sortOrder)).thenReturn(response);

        // When
        mockMvc
                .perform(
                        get(url)
                        .param("page",      String.valueOf(page))
                        .param("size",      String.valueOf(size))
                        .param("sortBy",    sortBy)
                        .param("sortOrder", sortOrder)
                        .contentType(MediaType.APPLICATION_JSON)
                )
        //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(response.getContent().size()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].personaId").value(response.getContent().get(0).getPersonaId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].personaId").value(response.getContent().get(1).getPersonaId()));
    }

    @Test
    void findAll_noParams() throws Exception {
        // Given
        int page = Integer.parseInt(PaginationUtils.PAGE_DEFAULT_VALUE);
        int size = Integer.parseInt(PaginationUtils.SIZE_DEFAULT_VALUE);
        String sortBy = PaginationUtils.CLIENTE_SORT_BY_DEFAULT_VALUE;
        String sortOrder = PaginationUtils.SORT_ORDER_DEFAULT_VALUE;

        List<ClienteResponseDTO> content =  Arrays.asList(
                ClienteResponseDTO.builder()
                        .personaId(1L)
                        .build(),
                ClienteResponseDTO.builder()
                        .personaId(2L)
                        .build()
        );

        PageImpl<ClienteResponseDTO> response = new PageImpl<>(
                content,
                PaginationUtils.createPageable(page, size, sortBy, sortOrder),
                content.size()
        );

        when(clienteService.findAll(page, size, sortBy, sortOrder)).thenReturn(response);

        // When
        mockMvc
                .perform(get(url).contentType(MediaType.APPLICATION_JSON))

                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(response.getContent().size()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].personaId").value(response.getContent().get(0).getPersonaId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].personaId").value(response.getContent().get(1).getPersonaId()));
    }

    @Test
    void findAll_badParams() throws Exception {
        // Given
        int page = -1; // <-- BAD VALUE
        int size = Integer.parseInt(PaginationUtils.SIZE_DEFAULT_VALUE);
        String sortBy = PaginationUtils.CLIENTE_SORT_BY_DEFAULT_VALUE;
        String sortOrder = PaginationUtils.SORT_ORDER_DEFAULT_VALUE;

        when(clienteService.findAll(page, size, sortBy, sortOrder)).thenThrow(IllegalPaginationArgumentException.class);

        // When
        mockMvc
                .perform(
                        get(url)
                                .param("page",      String.valueOf(page))
                                .param("size",      String.valueOf(size))
                                .param("sortBy",    sortBy)
                                .param("sortOrder", sortOrder)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById() throws Exception {
        // Given
        Long id = 1L;

        ClienteResponseDTO response = ClienteResponseDTO
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
                .cuentasId(null)
                .build();

        when(clienteService.findById(id)).thenReturn(response);

        // When
        mockMvc
                .perform(get(url+"/{id}", id).accept(MediaType.APPLICATION_JSON))

        // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personaId")      .value(response.getPersonaId()))
                .andExpect(jsonPath("$.nombre")         .value(response.getNombre()))
                .andExpect(jsonPath("$.genero")         .value(response.getGenero().toString()))
                .andExpect(jsonPath("$.edad")           .value(response.getEdad()))
                .andExpect(jsonPath("$.identificacion") .value(response.getIdentificacion()))
                .andExpect(jsonPath("$.direccion")      .value(response.getDireccion()))
                .andExpect(jsonPath("$.telefono")       .value(response.getTelefono()))
                .andExpect(jsonPath("$.contrasena")     .value(response.getContrasena()))
                .andExpect(jsonPath("$.estado")         .value(response.isEstado()))
                .andExpect(jsonPath("$.cuentasId")      .value(response.getCuentasId()));
    }

    @Test
    void findById_withInvalidId() throws Exception {
        // Given
        Long id = -1L;
        when(clienteService.findById(id)).thenThrow(NoSuchElementException.class);

        // When
        mockMvc
                .perform(get(url+"/{id}", id).accept(MediaType.APPLICATION_JSON))

        // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void update() throws Exception {
        // Given
        Long id = 1L;

        ClienteRequestDTO request = ClienteRequestDTO
                .builder()
                .nombre("Franco Becvort")
                .genero(Genero.MASCULINO)
                .edad(24)
                .identificacion("41809105")
                .direccion("Los Andes 1654")
                .telefono("234567890")
                .contrasena("password")
                .estado(true)
                .build();

        ClienteResponseDTO response = ClienteResponseDTO
                .builder()
                .personaId(id)
                .nombre("Franco Becvort")
                .genero(Genero.MASCULINO)
                .edad(24)
                .identificacion("41809105")
                .direccion("Los Andes 1654")
                .telefono("234567890")
                .contrasena("password")
                .estado(true)
                .cuentasId(null)
                .build();

        when(clienteService.update(id, request)).thenReturn(response);

        // When
        mockMvc
                .perform(
                        put(url+"/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.personaId")      .value(response.getPersonaId()))
                .andExpect(jsonPath("$.nombre")         .value(response.getNombre()))
                .andExpect(jsonPath("$.genero")         .value(response.getGenero().toString()))
                .andExpect(jsonPath("$.edad")           .value(response.getEdad()))
                .andExpect(jsonPath("$.identificacion") .value(response.getIdentificacion()))
                .andExpect(jsonPath("$.direccion")      .value(response.getDireccion()))
                .andExpect(jsonPath("$.telefono")       .value(response.getTelefono()))
                .andExpect(jsonPath("$.contrasena")     .value(response.getContrasena()))
                .andExpect(jsonPath("$.estado")         .value(response.isEstado()))
                .andExpect(jsonPath("$.cuentasId")      .value(response.getCuentasId()));
    }

    @Test
    void update_withInvalidId() throws Exception {
        // Given
        Long id = 1L;

        ClienteRequestDTO request = ClienteRequestDTO
                .builder()
                .nombre("Franco Becvort")
                .genero(Genero.MASCULINO)
                .edad(24)
                .identificacion("41809105")
                .direccion("Los Andes 1654")
                .telefono("234567890")
                .contrasena("password")
                .estado(true)
                .build();

        when(clienteService.update(id, request)).thenThrow(NoSuchElementException.class);

        // When
        mockMvc
                .perform(
                        put(url+"/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteById() throws Exception {
        // Given
        Long id = 1L;
        doNothing().when(clienteService).deleteById(id);

        // When
        mockMvc
                .perform(delete(url+"/{id}", id).contentType(MediaType.APPLICATION_JSON))

        // Then
                .andExpect(status().isOk())
                .andExpect(content().string("Cliente con id " + id + " fue eliminado"));

        verify(clienteService).deleteById(id);
    }

    @Test
    void deleteById_withCuentas() throws Exception {
        // Given
        Long id = -1L;
        doThrow(EntityWithAssociatedElementsException.class).when(clienteService).deleteById(id);

        // When
        mockMvc
                .perform(delete(url +"/{id}", id).contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteById_withInvalidId() throws Exception {
        // Given
        Long id = -1L;
        doThrow(NoSuchElementException.class).when(clienteService).deleteById(id);

        // When
        mockMvc
                .perform(delete(url +"/{id}", id).contentType(MediaType.APPLICATION_JSON))

        // Then
                .andExpect(status().isNotFound());
    }

    @Test
    void reporteCuentasDelCliente() throws Exception {
        // Given
        Long clienteId = 1L;

        String periodoStart = "2022-12-28T00:00:00.000+00:00";
        Date periodoStartDate = DateUtils.createDateFromString(periodoStart);

        String periodoEnd = "2022-12-31T00:00:00.000+00:00";
        Date periodoEndDate = DateUtils.createDateFromString(periodoEnd);

        ReporteCuentaResponseDTO reporteCuenta1 = ReporteCuentaResponseDTO.builder()
                .cuentaId(1L)
                .numeroCuenta(478758)
                .tipoCuenta(TipoCuenta.AHORROS)
                .saldo(BigDecimal.valueOf(2000.00))
                .estado(true)
                .periodoStart(periodoStartDate)
                .periodoEnd(periodoEndDate)
                .totalRetirosEnPeriodo(BigDecimal.valueOf(575.00))
                .totalDepositosEnPeriodo(BigDecimal.valueOf(120.00))
                .build();

        ReporteCuentaResponseDTO reporteCuenta2 = ReporteCuentaResponseDTO.builder()
                .cuentaId(2L)
                .numeroCuenta(585545)
                .tipoCuenta(TipoCuenta.CORRIENTE)
                .saldo(BigDecimal.valueOf(1000.00))
                .estado(true)
                .periodoStart(periodoStartDate)
                .periodoEnd(periodoEndDate)
                .totalRetirosEnPeriodo(BigDecimal.valueOf(0))
                .totalDepositosEnPeriodo(BigDecimal.valueOf(0))
                .build();

        List<ReporteCuentaResponseDTO> response = Arrays.asList(reporteCuenta1, reporteCuenta2);

        when(reporteService.makeReporte(clienteId, periodoStartDate, periodoEndDate)).thenReturn(response);

        // When
        mockMvc
                .perform(
                        get(url+"/{id}/reporte", clienteId)
                        .param("periodoStart", periodoStart)
                        .param("periodoEnd", periodoEnd)
                        .contentType(MediaType.APPLICATION_JSON)
                )

        // Then
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].cuentaId")                .value(response.get(0).getCuentaId()))
                .andExpect(jsonPath("$[0].numeroCuenta")            .value(response.get(0).getNumeroCuenta()))
                .andExpect(jsonPath("$[0].tipoCuenta")              .value(response.get(0).getTipoCuenta().toString()))
                .andExpect(jsonPath("$[0].saldo")                   .value(response.get(0).getSaldo().doubleValue()))
                .andExpect(jsonPath("$[0].estado")                  .value(response.get(0).isEstado()))
                .andExpect(jsonPath("$[0].periodoStart")            .value(response.get(0).getPeriodoStart().getTime()))
                .andExpect(jsonPath("$[0].periodoEnd")              .value(response.get(0).getPeriodoEnd().getTime()))
                .andExpect(jsonPath("$[0].totalRetirosEnPeriodo")   .value(response.get(0).getTotalRetirosEnPeriodo().doubleValue()))
                .andExpect(jsonPath("$[0].totalDepositosEnPeriodo") .value(response.get(0).getTotalDepositosEnPeriodo().doubleValue()))

                .andExpect(jsonPath("$[1].cuentaId")                .value(response.get(1).getCuentaId()))
                .andExpect(jsonPath("$[1].numeroCuenta")            .value(response.get(1).getNumeroCuenta()))
                .andExpect(jsonPath("$[1].tipoCuenta")              .value(response.get(1).getTipoCuenta().toString()))
                .andExpect(jsonPath("$[1].saldo")                   .value(response.get(1).getSaldo().doubleValue()))
                .andExpect(jsonPath("$[1].estado")                  .value(response.get(1).isEstado()))
                .andExpect(jsonPath("$[1].periodoStart")            .value(response.get(1).getPeriodoStart().getTime()))
                .andExpect(jsonPath("$[1].periodoEnd")              .value(response.get(1).getPeriodoEnd().getTime()))
                .andExpect(jsonPath("$[1].totalRetirosEnPeriodo")   .value(response.get(1).getTotalRetirosEnPeriodo().doubleValue()))
                .andExpect(jsonPath("$[1].totalDepositosEnPeriodo") .value(response.get(1).getTotalDepositosEnPeriodo().doubleValue()));

    }
}