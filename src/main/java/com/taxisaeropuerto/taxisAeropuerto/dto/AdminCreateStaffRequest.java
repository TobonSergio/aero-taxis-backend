package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class AdminCreateStaffRequest {
    @NotBlank
    private String username;

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
    private String cargo;

    @NotNull
    private Long rolId; // debe ser 2 para STAFF, pero podemos validar
}
