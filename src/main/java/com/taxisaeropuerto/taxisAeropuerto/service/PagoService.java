package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.dto.PagoRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.Pago;
import com.taxisaeropuerto.taxisAeropuerto.entity.Reserva;
import com.taxisaeropuerto.taxisAeropuerto.entity.Staff;
import com.taxisaeropuerto.taxisAeropuerto.repository.PagoRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.ReservaRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;
    private final StaffRepository staffRepository;

    public PagoService(PagoRepository pagoRepository,
                       ReservaRepository reservaRepository,
                       StaffRepository staffRepository) {
        this.pagoRepository = pagoRepository;
        this.reservaRepository = reservaRepository;
        this.staffRepository = staffRepository;
    }

    // 🔹 Crear un pago
    @Transactional
    public Pago crearPago(PagoRequest dto) {
        // Obtener reserva
        Reserva reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Obtener staff si viene en el request
        Staff staff = null;
        if (dto.getIdStaff() != null) {
            staff = staffRepository.findById(dto.getIdStaff())
                    .orElseThrow(() -> new RuntimeException("Staff no encontrado"));
        }

        // Crear pago
        Pago pago = new Pago();
        pago.setReserva(reserva);
        pago.setStaff(staff);
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstado(dto.getEstado() != null ? dto.getEstado() : Pago.EstadoPago.PENDIENTE);
        pago.setReferencia(dto.getReferencia());
        pago.setObservaciones(dto.getObservaciones());

        return pagoRepository.save(pago);
    }

    // 🔹 Listar todos los pagos
    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    // 🔹 Obtener pago por ID
    public Pago obtenerPagoPorId(Integer id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    // 🔹 Actualizar estado de un pago
    @Transactional
    public Pago actualizarEstadoPago(Integer id, Pago.EstadoPago nuevoEstado) {
        Pago pago = obtenerPagoPorId(id);
        pago.setEstado(nuevoEstado);

        // Si se confirma el pago, actualizar la reserva asociada
        if (nuevoEstado == Pago.EstadoPago.CONFIRMADO) {
            reservaRepository.findById(pago.getReserva().getId_reserva())
                    .ifPresent(reserva -> {
                        reserva.setEstado(Reserva.EstadoReserva.CONFIRMADA);
                        reservaRepository.save(reserva);
                    });
        }

        return pagoRepository.save(pago);
    }

    // 🔹 Listar pagos por reserva
    public List<Pago> listarPagosPorReserva(Integer idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        return pagoRepository.findByReserva(reserva);
    }

}
