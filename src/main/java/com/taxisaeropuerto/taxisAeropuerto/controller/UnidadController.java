package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.entity.Unidad;
import com.taxisaeropuerto.taxisAeropuerto.service.UnidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // ✅ Import necesario
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/unidades")
@RequiredArgsConstructor
public class UnidadController {

    private final UnidadService unidadService;

    // ✅ Solo ADMIN puede crear unidades
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Unidad> crearUnidad(@RequestBody Unidad unidad) {
        Unidad nuevaUnidad = unidadService.crearUnidad(unidad);
        return new ResponseEntity<>(nuevaUnidad, HttpStatus.CREATED);
    }

    // ✅ Cualquier usuario autenticado puede listar unidades
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<Unidad>> listarUnidades() {
        return ResponseEntity.ok(unidadService.listarUnidades());
    }

    // ✅ Cualquier usuario autenticado puede listar las disponibles
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/disponibles")
    public ResponseEntity<List<Unidad>> listarUnidadesDisponibles() {
        return ResponseEntity.ok(unidadService.listarUnidadesDisponibles());
    }

    // ✅ Solo ADMIN puede actualizar unidades
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Unidad> actualizarUnidad(@PathVariable Integer id, @RequestBody Unidad unidad) {
        Unidad actualizada = unidadService.actualizarUnidad(id, unidad);
        return ResponseEntity.ok(actualizada);
    }

    // ✅ Solo ADMIN puede eliminar unidades
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUnidad(@PathVariable Integer id) {
        unidadService.eliminarUnidad(id);
        return ResponseEntity.noContent().build();
    }
}
