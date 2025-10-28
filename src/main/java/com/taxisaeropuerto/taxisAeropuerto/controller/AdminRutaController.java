package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.RutaRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.Ruta;
import com.taxisaeropuerto.taxisAeropuerto.service.RutaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gestion/rutas") // 🔹 Ruta base privada
@RequiredArgsConstructor
public class AdminRutaController {

    private final RutaService rutaService;

    @PostMapping
    public ResponseEntity<Ruta> crearRuta(@Valid @RequestBody RutaRequest request) {
        Ruta ruta = new Ruta();
        ruta.setInicio(request.getInicio());
        ruta.setFin(request.getFin());
        ruta.setPrecio(request.getPrecio());

        return new ResponseEntity<>(rutaService.crearRuta(ruta), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Ruta>> listarRutas() {
        return ResponseEntity.ok(rutaService.listarRutas());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Ruta> obtenerRuta(@PathVariable Integer id) {
        return ResponseEntity.ok(rutaService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ruta> actualizarRuta(@PathVariable Integer id,
                                               @Valid @RequestBody RutaRequest request) {
        Ruta ruta = new Ruta();
        ruta.setInicio(request.getInicio());
        ruta.setFin(request.getFin());
        ruta.setPrecio(request.getPrecio());

        return ResponseEntity.ok(rutaService.actualizarRuta(id, ruta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRuta(@PathVariable Integer id) {
        rutaService.eliminarRuta(id);
        return ResponseEntity.noContent().build();
    }
}
