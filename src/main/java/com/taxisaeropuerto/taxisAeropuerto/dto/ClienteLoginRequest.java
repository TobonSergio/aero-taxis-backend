package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.Data;

@Data
public class ClienteLoginRequest {
    private String usernameOrEmail;
    private String password;
}
