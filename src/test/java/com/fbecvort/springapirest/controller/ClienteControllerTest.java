package com.fbecvort.springapirest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbecvort.springapirest.dto.cliente.ClienteRequestDTO;
import com.fbecvort.springapirest.dto.cliente.ClienteResponseDTO;
import com.fbecvort.springapirest.enumeration.Genero;
import com.fbecvort.springapirest.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

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
                        post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
        //Then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.personaId").value(response.getPersonaId()))
                .andExpect(jsonPath("$.nombre").value(response.getNombre()))
                .andExpect(jsonPath("$.edad").value(response.getEdad()));
    }

    @Test
    void findAll() {
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