package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.entity.Unidad;
import com.taxisaeropuerto.taxisAeropuerto.repository.UnidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnidadService {

    private final UnidadRepository unidadRepository;

    public Unidad crearUnidad(Unidad unidad) {
        return unidadRepository.save(unidad);
    }

    public List<Unidad> listarUnidades() {
        return unidadRepository.findAll();
    }

    public List<Unidad> listarUnidadesDisponibles() {
        return unidadRepository.findByEstado(Unidad.EstadoUnidad.DISPONIBLE);
    }

    public Unidad obtenerUnidadPorId(Integer id) {
        return unidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));
    }

    public Unidad actualizarUnidad(Integer id, Unidad unidadDetails) {
        Unidad unidad = obtenerUnidadPorId(id);

        unidad.setPlaca(unidadDetails.getPlaca());
        unidad.setSerie(unidadDetails.getSerie());
        unidad.setFotografia(unidadDetails.getFotografia());
        unidad.setEstado(unidadDetails.getEstado());

        return unidadRepository.save(unidad);
    }

    public void eliminarUnidad(Integer id) {
        Unidad unidad = obtenerUnidadPorId(id);
        unidadRepository.delete(unidad);
    }
}
