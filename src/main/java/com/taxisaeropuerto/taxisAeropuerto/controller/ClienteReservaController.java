package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.ReservaResponse;
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
@RequestMapping("/api/reservas") // Endpoints accesibles para clientes
@RequiredArgsConstructor
public class ClienteReservaController {

    private final ReservaService reservaService;

    // 🔹 Rutas inyectadas desde application.properties
    @Value("${custom.pdf-path}")
    private String pdfPath;

    @Value("${custom.qr-path}")
    private String qrPath;


    // 🔹 Crear nueva reserva
    @PostMapping
    public ResponseEntity<ReservaResponse> crearReserva(@RequestBody ReservaRequest request) {
        System.out.println("📦 Reserva recibida: " + request);
        ReservaResponse reservaResponse = reservaService.crearReserva(request);
        return ResponseEntity.ok(reservaResponse);
    }

    // 🔹 Listar reservas del cliente
    @GetMapping("/mis-reservas/{idCliente}")
    public ResponseEntity<List<Reserva>> listarMisReservas(@PathVariable Integer idCliente) {
        List<Reserva> reservas = reservaService.listarReservasPorCliente(idCliente);
        return ResponseEntity.ok(reservas);
    }

    // 🔹 Obtener detalles de una reserva específica
    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerReserva(@PathVariable Integer id) {
        Reserva reserva = reservaService.obtenerReservaPorId(id);
        return ResponseEntity.ok(reserva);
    }

    // Descargar PDF
    @GetMapping("/pdf/{id}")
    public ResponseEntity<Resource> descargarPdf(@PathVariable Integer id) throws IOException {
        Reserva reserva = reservaService.obtenerReservaPorId(id);
        Path filePath = Paths.get(pdfPath + reserva.getComprobantePdf()); // 🔹 usa la ruta de properties
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // Descargar QR
    @GetMapping("/qr/{id}")
    public ResponseEntity<Resource> descargarQr(@PathVariable Integer id) throws IOException {
        Reserva reserva = reservaService.obtenerReservaPorId(id);
        Path filePath = Paths.get(qrPath + reserva.getQrCode()); // 🔹 usa la ruta de properties
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


}
