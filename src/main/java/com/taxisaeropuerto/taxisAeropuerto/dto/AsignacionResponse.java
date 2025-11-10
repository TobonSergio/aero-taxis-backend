package com.taxisaeropuerto.taxisAeropuerto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignacionResponse {

    private Integer idAsignacion;

    // Datos de la reserva
    private Integer idReserva;
    private String destino;
    private String estadoReserva;

    // Datos del cliente
    private String nombreCliente;
    private String apellidoCliente;

    // Datos del chofer y unidad
    private String nombreChofer;
    private String apellidoChofer;
    private String placaUnidad;

    private LocalDateTime fechaAsignacion;
    private String estadoAsignacion;
    // 🧾 Nuevo campo para incluir el comprobante PDF
    private String comprobantePdf;
}
