package com.taxisaeropuerto.taxisAeropuerto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Asignaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Integer idAsignacion;

    @ManyToOne
    @JoinColumn(name = "id_reserva", nullable = false)
    private Reserva reserva;

    @ManyToOne
    @JoinColumn(name = "id_unidad", nullable = true)
    private Unidad unidad;

    @ManyToOne
    @JoinColumn(name = "id_chofer", nullable = true)
    private Chofer chofer;

    // ✅ Nuevo campo para guardar la ruta o nombre del PDF generado
    @Column(name = "pdf_path")
    private String pdfPath;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAsignacion estado;

    public enum EstadoAsignacion {
        PENDIENTE,
        ACTIVA,
        FINALIZADA,
        CANCELADA
    }
}
