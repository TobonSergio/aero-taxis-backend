package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.UnidadRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.Unidad;
import com.taxisaeropuerto.taxisAeropuerto.service.UnidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestion/unidades")
@RequiredArgsConstructor
public class AdminUnidadController {

    private final UnidadService unidadService;

    @PostMapping
    public ResponseEntity<Unidad> crearUnidad(@Valid @RequestBody UnidadRequest request) {
        Unidad creada = unidadService.crearUnidad(request);
        return ResponseEntity.ok(creada);
    }

    @GetMapping
    public ResponseEntity<List<Unidad>> listarUnidades() {
        return ResponseEntity.ok(unidadService.listarUnidades());
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Unidad>> listarUnidadesDisponibles() {
        return ResponseEntity.ok(unidadService.listarUnidadesDisponibles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Unidad> obtenerUnidad(@PathVariable Integer id) {
        return ResponseEntity.ok(unidadService.obtenerUnidadPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Unidad> actualizarUnidad(@PathVariable Integer id,
                                                   @Valid @RequestBody UnidadRequest request) {
        Unidad unidad = new Unidad();
        unidad.setPlaca(request.getPlaca());
        unidad.setSerie(request.getSerie());
        unidad.setFotografia(request.getFotografia());
        unidad.setEstado(request.getEstado());

        return ResponseEntity.ok(unidadService.actualizarUnidad(id, unidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUnidad(@PathVariable Integer id) {
        unidadService.eliminarUnidad(id);
        return ResponseEntity.noContent().build();
    }
}
