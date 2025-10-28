package com.taxisaeropuerto.taxisAeropuerto.repository;

import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Método para buscar un usuario por su nombre de usuario
    Optional<User> findByUsername(String username);
    Optional<User> findByCorreo(String correo);
    // 🔹 Buscar por username o correo (para login unificado)
    Optional<User> findByUsernameOrCorreo(String username, String correo);
    // Método para buscar un usuario por su token de verificación
    Optional<User> findByVerificationToken(String verificationToken);

    // 🔹 Agrega estos dos métodos para las validaciones
    boolean existsByCorreo(String correo);
    boolean existsByUsername(String username);
}
