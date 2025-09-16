package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.Data;

@Data
public class AdminCreateUserRequest {
    private String name;
    private String lastName;
    private String email;
    private String number;
    private String username;
    private String password;
    private Long rolId;  // ej: "ADMIN" o "EMPLEADO"
}