package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequest {
    private Integer idCliente;
    private Integer idStaff; // opcional
    private String lugarRecogida;
    private String destino;
    private Integer idRuta;
    private LocalDateTime fechaReserva;
    private LocalTime horaReserva;
    private String comentarios; // opcional
}
