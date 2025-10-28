package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.ReservaRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.Reserva;
import com.taxisaeropuerto.taxisAeropuerto.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestion/reservas") // Endpoints privados para staff/admin
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','STAFF')")
public class StaffReservaController {

    private final ReservaService reservaService;

    // 🔹 Listar todas las reservas
    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {
        List<Reserva> reservas = reservaService.listarReservas();
        return ResponseEntity.ok(reservas);
    }

    // 🔹 Obtener detalles de una reserva
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerReserva(@PathVariable Integer id) {
        Reserva reserva = reservaService.obtenerReservaPorId(id);
        return ResponseEntity.ok(reserva);
    }

    // 🔹 Actualizar reserva (por ejemplo asignar staff o cambiar estado)
    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizarReserva(@PathVariable Integer id,
                                                     @RequestBody ReservaRequest request) {
        Reserva reservaActualizada = reservaService.actualizarReserva(id, request);
        return ResponseEntity.ok(reservaActualizada);
    }

    // 🔹 Eliminar reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Integer id) {
        reservaService.eliminarReserva(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<Reserva>> listarReservasPendientes() {
        List<Reserva> pendientes = reservaService.listarReservasPorEstado(Reserva.EstadoReserva.PENDIENTE);
        return ResponseEntity.ok(pendientes);
    }

}
