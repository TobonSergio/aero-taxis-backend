package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponse {

    private Integer idStaff;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String cargo;
    private String rolNombre;
    private String username;

}
