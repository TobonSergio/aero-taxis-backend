package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private Long idUsuario;
    private String rol;
    private Long idPerfil; // 👈 idCliente, idStaff o idChofer según el rol
}
