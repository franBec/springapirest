package com.fbecvort.springapirest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbecvort.springapirest.dto.cliente.ClienteRequestDTO;
import com.fbecvort.springapirest.dto.cliente.ClienteResponseDTO;
import com.fbecvort.springapirest.enumeration.Genero;
import com.fbecvort.springapirest.exception.crud.IllegalPaginationArgumentException;
import com.fbecvort.springapirest.service.ClienteService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {
    final private String url = "/api/clientes";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ClienteService clienteService;

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
                        MockMvcRequestBuilders.get(url)
                        .param("page",      String.valueOf(page))
                        .param("size",      String.valueOf(size))
                        .param("sortBy",    sortBy)
                        .param("sortOrder", sortOrder)
                        .contentType(MediaType.APPLICATION_JSON)
                )
        //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
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
                .perform(
                        MockMvcRequestBuilders.get(url)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                //Then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(response.getContent().size()))

                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].personaId").value(response.getContent().get(0).getPersonaId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].personaId").value(response.getContent().get(1).getPersonaId()));
    }

    @Test
    void findById() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void reporteCuentasDelCliente() {
    }
}