package com.taxisaeropuerto.taxisAeropuerto.repository;

import com.taxisaeropuerto.taxisAeropuerto.entity.Staff;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    // Buscar staff por correo
    Optional<Staff> findByCorreo(String correo);

    // Buscar staff por la entidad User (relación directa)
    Optional<Staff> findByUsuario(User usuario);
    Optional<Staff> findByUsuarioId(Long usuarioId);

    // Alternativa: buscar por correo dentro del usuario relacionado
    Optional<Staff> findByUsuario_Correo(String correo);
}
