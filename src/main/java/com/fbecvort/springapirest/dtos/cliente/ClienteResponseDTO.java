package com.fbecvort.springapirest.dtos.cliente;

import com.fbecvort.springapirest.enums.Genero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDTO {
    private Long personaId;
    private String nombre;
    private Genero genero;
    private int edad;
    private String identificacion;
    private String direccion;
    private String telefono;
    private String contrasena;
    private boolean estado;
    private Set<Long> cuentasId;
}
