package com.taxisaeropuerto.taxisAeropuerto.repository;

import com.taxisaeropuerto.taxisAeropuerto.entity.Pago;
import com.taxisaeropuerto.taxisAeropuerto.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    // 🔹 Buscar todos los pagos asociados a una reserva
    List<Pago> findByReserva(Reserva reserva);

}
