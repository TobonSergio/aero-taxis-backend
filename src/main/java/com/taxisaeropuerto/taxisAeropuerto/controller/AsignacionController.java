package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.AsignacionRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.Asignacion;
import com.taxisaeropuerto.taxisAeropuerto.service.AsignacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestion/asignaciones")
@RequiredArgsConstructor
public class AsignacionController {

    private final AsignacionService asignacionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<Asignacion> crearAsignacion(@RequestBody AsignacionRequest dto) {
        return ResponseEntity.ok(asignacionService.crearAsignacion(dto));
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
}
