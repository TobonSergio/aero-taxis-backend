package com.taxisaeropuerto.taxisAeropuerto.repository;

import com.taxisaeropuerto.taxisAeropuerto.entity.Asignacion;
import com.taxisaeropuerto.taxisAeropuerto.entity.Chofer;
import com.taxisaeropuerto.taxisAeropuerto.entity.Reserva;
import com.taxisaeropuerto.taxisAeropuerto.entity.Unidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsignacionRepository extends JpaRepository<Asignacion, Integer> {

    Optional<Asignacion> findByReserva(Reserva reserva);

    // 🔹 Verifica si un chofer tiene asignaciones pendientes o activas
    boolean existsByChoferAndEstadoIn(Chofer chofer, List<Asignacion.EstadoAsignacion> estados);

    // 🔹 Verifica si una unidad está ocupada en asignaciones pendientes o activas
    boolean existsByUnidadAndEstadoIn(Unidad unidad, List<Asignacion.EstadoAsignacion> estados);
    List<Asignacion> findByEstado(Asignacion.EstadoAsignacion estado);
    Optional<Asignacion> findByReserva_IdReserva(Integer idReserva);
}
