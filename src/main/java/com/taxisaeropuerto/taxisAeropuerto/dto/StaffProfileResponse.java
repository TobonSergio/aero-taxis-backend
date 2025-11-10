package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffProfileResponse {

    private Integer idStaff;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String cargo;
    private Long idUsuario;
    private String username;
    private String rolName;
}
