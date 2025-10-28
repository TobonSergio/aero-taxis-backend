package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.PagoRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.Pago;
import com.taxisaeropuerto.taxisAeropuerto.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    // 🔹 Crear un pago
    @PostMapping
    public ResponseEntity<Pago> crearPago(@RequestBody PagoRequest dto) {
        return ResponseEntity.ok(pagoService.crearPago(dto));
    }

    // 🔹 Listar todos los pagos
    @GetMapping
    public ResponseEntity<List<Pago>> listarPagos() {
        return ResponseEntity.ok(pagoService.listarPagos());
    }

    // 🔹 Obtener pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPago(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoService.obtenerPagoPorId(id));
    }

    // 🔹 Actualizar estado de un pago
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pago> actualizarEstado(@PathVariable Integer id, @RequestParam Pago.EstadoPago estado) {
        return ResponseEntity.ok(pagoService.actualizarEstadoPago(id, estado));
    }

    // 🔹 Listar pagos de una reserva específica
    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<Pago>> listarPagosPorReserva(@PathVariable Integer idReserva) {
        return ResponseEntity.ok(pagoService.listarPagosPorReserva(idReserva));
    }
}
