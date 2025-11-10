package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.AsignacionRequest;
import com.taxisaeropuerto.taxisAeropuerto.dto.AsignacionResponse;
import com.taxisaeropuerto.taxisAeropuerto.entity.Asignacion;
import com.taxisaeropuerto.taxisAeropuerto.service.AsignacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/gestion/asignaciones")
@RequiredArgsConstructor
public class AsignacionController {

    private final AsignacionService asignacionService;

    @Value("${custom.pdf-path}")
    private String pdfPath;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<AsignacionResponse> crearAsignacion(@RequestBody AsignacionRequest dto) {
        AsignacionResponse response = asignacionService.crearAsignacion(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<List<Asignacion>> listarAsignaciones() {
        return ResponseEntity.ok(asignacionService.listarAsignaciones());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Asignacion> obtenerAsignacion(@PathVariable Integer id) {
        return ResponseEntity.ok(asignacionService.obtenerPorId(id));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Asignacion> actualizarEstado(@PathVariable Integer id,
                                                       @RequestParam Asignacion.EstadoAsignacion estado) {
        return ResponseEntity.ok(asignacionService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Void> eliminarAsignacion(@PathVariable Integer id) {
        asignacionService.eliminarAsignacion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pdf/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Resource> descargarPdf(@PathVariable Integer id) throws IOException {
        Asignacion asignacion = asignacionService.obtenerPorId(id); // obtenemos la asignación
        Path filePath = Paths.get(pdfPath + asignacion.getReserva().getComprobantePdf()); // PDF generado para la reserva asociada
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new IOException("El archivo PDF no se encuentra o no se puede leer: " + filePath);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }



}
