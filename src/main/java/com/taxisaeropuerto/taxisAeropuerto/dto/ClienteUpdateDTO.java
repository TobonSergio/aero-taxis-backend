package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.Data;

@Data
public class ClienteUpdateDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String idioma;
    private String fechaNacimiento;
    private String genero;
    private String password; // opcional, para actualizar contraseña
}
