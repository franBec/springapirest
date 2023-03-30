package com.fbecvort.springapirest.dto.cliente;

import com.fbecvort.springapirest.enumeration.Genero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteRequestDTO {
    @NotBlank(message = "nombre no debe estar vacío")
    @Size(max = 127, message = "nombre debe tener como máximo {max} caracteres")
    private String nombre;

    @NotNull(message = "genero no debe ser nulo")
    private Genero genero;

    @Positive(message = "edad debe ser un entero positivo")
    private int edad;

    @NotBlank(message = "identificacion no debe estar vacía")
    @Size(max = 15, message = "identificacion debe tener como máximo {max} caracteres")
    private String identificacion;

    @NotBlank(message = "direccion no debe estar vacía")
    @Size(max = 127, message = "direccion debe tener como máximo {max} caracteres")
    private String direccion;

    @NotBlank(message = "telefono no debe estar vacío")
    @Size(max = 31, message = "telefono debe tener como máximo {max} caracteres")
    private String telefono;

    @NotBlank(message = "contrasena no debe estar vacía")
    @Size(max = 255, message = "contrasena debe tener como máximo {max} caracteres")
    private String contrasena;

    @NotNull(message = "estado no debe ser nulo")
    private boolean estado;
}
