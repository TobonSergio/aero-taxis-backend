package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaListClienteResponse {
    private Integer idReserva;
    private String lugarRecogida;
    private String destino;
    private LocalDateTime fechaReserva;
    private LocalTime horaReserva;
    private String estado;
    private String comentarios;
    private String nombreRuta; // opcional, si quieres mostrar el nombre o el destino de la ruta
}
