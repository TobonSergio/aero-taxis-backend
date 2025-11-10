package com.taxisaeropuerto.taxisAeropuerto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "choferes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chofer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chofer")
    private Integer idChofer;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "correo", unique = true)
    private String correo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "licencia_conduccion", nullable = false)
    private String licenciaConduccion;

    @Column(name = "bilingue")
    private Boolean bilingue = false;

    // ✅ Enum para manejar estados
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoChofer estado = EstadoChofer.DISPONIBLE;

    @OneToOne
    @JoinColumn(name = "fk_id_usuario", referencedColumnName = "id_usuario")
    private User usuario;

    public enum EstadoChofer {
        DISPONIBLE,
        OCUPADO,
        INACTIVO
    }
}
