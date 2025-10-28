package com.taxisaeropuerto.taxisAeropuerto.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_staff")
    private Integer idStaff;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "correo", unique = true)
    private String correo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "cargo")
    private String cargo; // Ejemplo: "Administrador", "Soporte", "Operador"

    @OneToOne
    @JoinColumn(name = "fk_id_usuario", referencedColumnName = "id_usuario")
    private User usuario;
}
