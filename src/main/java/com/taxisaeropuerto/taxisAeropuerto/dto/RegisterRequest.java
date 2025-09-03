package com.taxisaeropuerto.taxisAeropuerto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    private String name;

    private String lastName;

    private String email;

    private String number;

    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // Si quieres un campo para confirmar la contraseña
    // @NotBlank(message = "La confirmación de la contraseña es obligatoria")
    // private String confirmPassword;
}