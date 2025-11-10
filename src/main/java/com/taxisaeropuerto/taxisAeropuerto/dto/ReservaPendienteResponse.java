package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaPendienteResponse {
    private Integer idReserva;
    private String fechaReserva;
    private String estado;
    private String nombreCliente;
    private String apellidoCliente;
    private String telefonoCliente;
    private String ciudadCliente;
    private String origen;
    private String destino;
}