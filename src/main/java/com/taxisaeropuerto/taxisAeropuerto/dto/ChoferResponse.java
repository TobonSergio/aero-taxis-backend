package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoferResponse {
    private Integer idChofer;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String licenciaConduccion;
    private Boolean bilingue;
    private String estado;
    private String username;
    private String rolNombre;
}
