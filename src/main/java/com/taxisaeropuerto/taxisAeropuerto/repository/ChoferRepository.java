package com.taxisaeropuerto.taxisAeropuerto.repository;

import com.taxisaeropuerto.taxisAeropuerto.entity.Chofer;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChoferRepository extends JpaRepository<Chofer, Integer> {

    // Buscar chofer por correo
    Optional<Chofer> findByCorreo(String correo);

    // Buscar chofer por la entidad User (relación directa)
    Optional<Chofer> findByUsuario(User usuario);

    Optional<Chofer> findByUsuarioId(Long usuarioId);

    // Buscar chofer por correo dentro del usuario relacionado
    Optional<Chofer> findByUsuario_Correo(String correo);

    // ✅ Nuevo método: listar choferes por estado
    List<Chofer> findByEstado(Chofer.EstadoChofer estado);

}
