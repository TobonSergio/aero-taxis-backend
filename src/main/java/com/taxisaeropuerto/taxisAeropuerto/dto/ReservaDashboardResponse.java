package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDashboardResponse {

    private Integer idReserva;
    private String nombreCliente;
    private String destino;
    private LocalDateTime fechaReserva;
    private String estado;
}
