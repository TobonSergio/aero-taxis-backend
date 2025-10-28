package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.Data;

@Data
public class ClienteRegisterRequest {
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String password;
}
