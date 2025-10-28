package com.taxisaeropuerto.taxisAeropuerto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pago;

    // 🔹 Relación con la reserva (cada pago pertenece a una reserva)
    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    // 🔹 Relación con el staff (quien procesó o confirmó el pago)
    @ManyToOne
    @JoinColumn(name = "id_staff")
    private Staff staff; // opcional si el cliente paga directamente

    // 🔹 Monto total del pago
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    // 🔹 Método de pago (efectivo, tarjeta, transferencia, etc.)
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false, length = 50)
    private MetodoPago metodoPago;

    // 🔹 Estado del pago (pendiente, confirmado, reembolsado, etc.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    // 🔹 Fecha y hora del pago
    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago = LocalDateTime.now();

    // 🔹 Código de referencia (para pagos digitales o bancarios)
    @Column(name = "referencia", length = 100)
    private String referencia;

    // 🔹 Observaciones o comentarios opcionales
    @Column(name = "observaciones", length = 255)
    private String observaciones;

    // 🔹 Tipos de pago disponibles
    public enum MetodoPago {
        EFECTIVO,
        TARJETA,
        TRANSFERENCIA,
        PSE,
        OTRO
    }

    // 🔹 Estados del pago
    public enum EstadoPago {
        PENDIENTE,
        CONFIRMADO,
        REEMBOLSADO,
        CANCELADO
    }
}
