package com.taxisaeropuerto.taxisAeropuerto.dto;

import com.taxisaeropuerto.taxisAeropuerto.entity.Pago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoRequest {
    private Integer idReserva;
    private Integer idStaff; // opcional, si el staff registra el pago
    private BigDecimal monto;
    private Pago.MetodoPago metodoPago;
    private Pago.EstadoPago estado; // PENDIENTE o CONFIRMADO
    private String referencia; // opcional, para pagos digitales
    private String observaciones; // opcional
}
