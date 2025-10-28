package com.taxisaeropuerto.taxisAeropuerto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminUpdateUserRequest {

    @NotBlank(message = "El nombre es obligatorio.")
    private String name;

    @NotBlank(message = "El apellido es obligatorio.")
    private String lastName;

    @Email(message = "El correo debe tener un formato válido.")
    @NotBlank(message = "El correo es obligatorio.")
    private String email;

    @NotBlank(message = "El número de contacto es obligatorio.")
    private String number;

    private String username;  // 👈 agregado
    private String password;  // 👈 agregado

    @NotNull(message = "Debe seleccionar un rol.")
    private Long rolId;
}
