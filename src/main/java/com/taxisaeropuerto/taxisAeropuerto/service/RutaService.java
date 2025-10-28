package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.entity.Ruta;
import com.taxisaeropuerto.taxisAeropuerto.repository.RutaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RutaService {

    private final RutaRepository rutaRepository;

    public RutaService(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    public Ruta crearRuta(Ruta ruta) {
        return rutaRepository.save(ruta);
    }

    public List<Ruta> listarRutas() {
        return rutaRepository.findAll();
    }

    public Ruta obtenerPorId(Integer id) {
        return rutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));
    }

    public Ruta actualizarRuta(Integer id, Ruta datos) {
        Ruta ruta = obtenerPorId(id);
        ruta.setInicio(datos.getInicio());
        ruta.setFin(datos.getFin());
        ruta.setPrecio(datos.getPrecio());
        return rutaRepository.save(ruta);
    }

    public void eliminarRuta(Integer id) {
        Ruta ruta = obtenerPorId(id);
        rutaRepository.delete(ruta);
    }
}
