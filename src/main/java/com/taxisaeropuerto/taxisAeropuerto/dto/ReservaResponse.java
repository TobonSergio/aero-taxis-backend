package com.taxisaeropuerto.taxisAeropuerto.dto;

import com.taxisaeropuerto.taxisAeropuerto.entity.Reserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponse {

    private Integer idReserva;
    private Integer idCliente;
    private Integer idRuta;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String email;
    private LocalDateTime fechaReserva;
    private String comentarios;

}
