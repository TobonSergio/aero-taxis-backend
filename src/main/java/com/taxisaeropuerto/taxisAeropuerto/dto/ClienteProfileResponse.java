package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteProfileResponse {
    private Integer idCliente;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String idioma;
    private String fechaNacimiento;
    private String genero;
    private Long idUsuario;
    private String username;
    private String rolName;
}
