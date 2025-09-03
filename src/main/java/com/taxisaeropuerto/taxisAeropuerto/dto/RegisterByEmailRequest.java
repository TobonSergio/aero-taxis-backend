package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class RegisterByEmailRequest {
    @NotBlank(message = "El correo no puede estar en blanco")
    @Email(message = "Formato de correo inválido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar en blanco")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
}