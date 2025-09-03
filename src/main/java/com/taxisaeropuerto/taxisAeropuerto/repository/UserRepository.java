package com.taxisaeropuerto.taxisAeropuerto.repository;

import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Método para buscar un usuario por su nombre de usuario
    Optional<User> findByUsername(String username);

    // Método para buscar un usuario por su correo electrónico
    Optional<User> findByEmail(String email);

    // Método para buscar un usuario por su token de verificación
    Optional<User> findByVerificationToken(String verificationToken);
}
