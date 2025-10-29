package com.taxisaeropuerto.taxisAeropuerto.repository;

import com.taxisaeropuerto.taxisAeropuerto.entity.Cliente;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    // Buscar cliente por correo
    Optional<Cliente> findByCorreo(String correo);

    // Buscar cliente por la entidad User (relación directa)
    Optional<Cliente> findByUsuario(User usuario);


    // Alternativa: buscar por el correo dentro del usuario relacionado
    Optional<Cliente> findByUsuario_Correo(String correo);
    Optional<Cliente> findByUsuarioId(Long usuarioId);

}
