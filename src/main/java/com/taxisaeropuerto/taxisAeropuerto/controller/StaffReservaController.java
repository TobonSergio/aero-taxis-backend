package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.ReservaDashboardResponse;
import com.taxisaeropuerto.taxisAeropuerto.dto.ReservaPendienteResponse;
import com.taxisaeropuerto.taxisAeropuerto.dto.ReservaRequest;
import com.taxisaeropuerto.taxisAeropuerto.dto.ReservaResponse;
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

    @GetMapping
    public ResponseEntity<List<ReservaResponse>> listarReservas() {
        List<ReservaResponse> reservas = reservaService.listarReservas();
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
    public ResponseEntity<List<ReservaPendienteResponse>> listarReservasPendientes() {
        List<ReservaPendienteResponse> pendientes = reservaService.listarReservasPendientes();
        return ResponseEntity.ok(pendientes);
    }

    // Endpoint para el dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<List<ReservaDashboardResponse>> obtenerReservasDashboard() {
        List<ReservaDashboardResponse> dashboard = reservaService.listarReservasDashboard();
        return ResponseEntity.ok(dashboard);
    }


}
