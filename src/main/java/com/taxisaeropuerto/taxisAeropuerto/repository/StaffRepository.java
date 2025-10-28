package com.taxisaeropuerto.taxisAeropuerto.repository;

import com.taxisaeropuerto.taxisAeropuerto.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    // 🔹 Buscar un staff por correo
    Optional<Staff> findByCorreo(String correo);
    Optional<Staff> findByUsuario_Correo(String correo);
}
