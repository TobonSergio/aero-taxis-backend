package com.taxisaeropuerto.taxisAeropuerto.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChoferCreateRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String correo;

    @NotBlank
    private String telefono;

    @NotBlank
    private String licenciaConduccion;

    private Boolean bilingue;
}
