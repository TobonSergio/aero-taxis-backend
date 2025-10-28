package com.taxisaeropuerto.taxisAeropuerto.repository;

import com.taxisaeropuerto.taxisAeropuerto.entity.Chofer;
import com.taxisaeropuerto.taxisAeropuerto.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChoferRepository extends JpaRepository<Chofer, Integer> {
    Optional<Chofer> findByUsuario_Correo(String correo);

}
