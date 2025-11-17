package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.ReservaListClienteResponse;
import com.taxisaeropuerto.taxisAeropuerto.dto.ReservaResponse;
import com.taxisaeropuerto.taxisAeropuerto.entity.Asignacion;
import com.taxisaeropuerto.taxisAeropuerto.repository.AsignacionRepository;
import com.taxisaeropuerto.taxisAeropuerto.service.PDFService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource; // ✅ correcto
import com.taxisaeropuerto.taxisAeropuerto.dto.ReservaRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.Reserva;
import com.taxisaeropuerto.taxisAeropuerto.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ClienteReservaController {

    private final ReservaService reservaService;
    private final AsignacionRepository asignacionRepository;
    private final PDFService pdfService;  // ✅ AÑADIDO AQUÍ


    // Crear nueva reserva
    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(@RequestBody ReservaRequest request) {
        ReservaResponse reservaResponse = reservaService.crearReserva(request);
        return ResponseEntity.ok(reservaResponse);
    }

    // Listar reservas del cliente
    @GetMapping("/mis-reservas/{idCliente}")
    public ResponseEntity<List<ReservaListClienteResponse>> listarMisReservas(@PathVariable Integer idCliente) {
        List<ReservaListClienteResponse> reservasListClienteResponse =
                reservaService.listarReservasPorCliente(idCliente);
        return ResponseEntity.ok(reservasListClienteResponse);
    }

    // Obtener reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerReserva(@PathVariable Integer id) {
        Reserva reserva = reservaService.obtenerReservaPorId(id);
        return ResponseEntity.ok(reserva);
    }

    // Descargar PDF desde memoria
    @GetMapping("/pdf/{idReserva}")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Integer idReserva) {

        Asignacion asignacion = asignacionRepository.findByReserva_IdReserva(idReserva)
                .orElseThrow(() -> new RuntimeException("No hay asignación"));

        byte[] pdfBytes = pdfService.generarComprobante(asignacion);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comprobante_" + idReserva + ".pdf")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(pdfBytes);
    }

}
