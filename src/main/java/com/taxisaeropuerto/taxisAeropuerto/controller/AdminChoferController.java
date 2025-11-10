package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.ChoferCreateRequest;
import com.taxisaeropuerto.taxisAeropuerto.dto.ChoferResponse;
import com.taxisaeropuerto.taxisAeropuerto.entity.Chofer;
import com.taxisaeropuerto.taxisAeropuerto.service.ChoferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestion/choferes")
@RequiredArgsConstructor
public class AdminChoferController {

    private final ChoferService choferService;

    @PostMapping
    public ResponseEntity<Chofer> crearChofer(@RequestBody ChoferCreateRequest request) {
        return ResponseEntity.ok(choferService.crearChofer(request));
    }


    @GetMapping
    public ResponseEntity<List<Chofer>> listarChoferes() {
        return ResponseEntity.ok(choferService.listarChoferes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chofer> obtenerChofer(@PathVariable Integer id) {
        return ResponseEntity.ok(choferService.obtenerChoferPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chofer> actualizarChofer(@PathVariable Integer id, @RequestBody ChoferCreateRequest request) {
        return ResponseEntity.ok(choferService.actualizarChofer(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarChofer(@PathVariable Integer id) {
        choferService.eliminarChofer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<ChoferResponse>> listarChoferesDisponibles() {
        return ResponseEntity.ok(choferService.listarChoferesDisponibles());
    }

}
