package com.taxisaeropuerto.taxisAeropuerto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Unidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidad")
    private Integer idUnidad;

    @Column(nullable = false, unique = true)
    private String placa;

    @Column(nullable = false, unique = true)
    private String serie;

    private String fotografia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoUnidad estado;

    public enum EstadoUnidad {
        DISPONIBLE,
        OCUPADA,
        MANTENIMIENTO
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTaxi tipoTaxi;
    public enum TipoTaxi {
        NORMAL,
        TURISTICO
    }
}
