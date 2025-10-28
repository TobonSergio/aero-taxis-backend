package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.entity.Ruta;
import com.taxisaeropuerto.taxisAeropuerto.service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RutaController {

    private final RutaService rutaService;

    // =======================
    // Endpoints públicos
    // =======================
    @GetMapping("/api/rutas")
    public ResponseEntity<List<Ruta>> listarRutas() {
        return ResponseEntity.ok(rutaService.listarRutas());
    }

    @GetMapping("/api/rutas/{id}")
    public ResponseEntity<Ruta> obtenerRuta(@PathVariable Integer id) {
        return ResponseEntity.ok(rutaService.obtenerPorId(id));
    }

    // =======================
    // Endpoints de administración (solo ADMIN o STAFF)
    // =======================
    @PostMapping("/api/rutas")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<Ruta> crearRuta(@RequestBody Ruta ruta) {
        return new ResponseEntity<>(rutaService.crearRuta(ruta), HttpStatus.CREATED);
    }

    @PutMapping("/api/rutas/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<Ruta> actualizarRuta(@PathVariable Integer id, @RequestBody Ruta ruta) {
        return ResponseEntity.ok(rutaService.actualizarRuta(id, ruta));
    }

    @DeleteMapping("/api/rutas/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
    public ResponseEntity<?> eliminarRuta(@PathVariable Integer id) {
        rutaService.eliminarRuta(id);
        return ResponseEntity.ok("Ruta eliminada correctamente");
    }
}
