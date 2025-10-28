package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.dto.AsignacionRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.Asignacion;
import com.taxisaeropuerto.taxisAeropuerto.entity.Chofer;
import com.taxisaeropuerto.taxisAeropuerto.entity.Reserva;
import com.taxisaeropuerto.taxisAeropuerto.entity.Unidad;
import com.taxisaeropuerto.taxisAeropuerto.repository.AsignacionRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.ChoferRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.ReservaRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.UnidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final ReservaRepository reservaRepository;
    private final ChoferRepository choferRepository;
    private final UnidadRepository unidadRepository;

    public AsignacionService(AsignacionRepository asignacionRepository,
                             ReservaRepository reservaRepository,
                             ChoferRepository choferRepository,
                             UnidadRepository unidadRepository) {
        this.asignacionRepository = asignacionRepository;
        this.reservaRepository = reservaRepository;
        this.choferRepository = choferRepository;
        this.unidadRepository = unidadRepository;
    }

    @Transactional
    public Asignacion crearAsignacion(AsignacionRequest dto) {
        // Obtener reserva
        Reserva reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // Obtener chofer y validar disponibilidad
        Chofer chofer = choferRepository.findById(dto.getIdChofer())
                .orElseThrow(() -> new RuntimeException("Chofer no encontrado"));
        if(asignacionRepository.existsByChoferAndEstadoIn(chofer, List.of(Asignacion.EstadoAsignacion.PENDIENTE, Asignacion.EstadoAsignacion.ACTIVA))) {
            throw new RuntimeException("Chofer ya tiene una asignación activa o pendiente");
        }

        // Obtener unidad y validar disponibilidad
        Unidad unidad = unidadRepository.findById(dto.getIdUnidad())
                .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));
        if(asignacionRepository.existsByUnidadAndEstadoIn(unidad, List.of(Asignacion.EstadoAsignacion.PENDIENTE, Asignacion.EstadoAsignacion.ACTIVA))) {
            throw new RuntimeException("Unidad ya está ocupada");
        }

        // Crear asignación
        Asignacion asignacion = new Asignacion();
        asignacion.setReserva(reserva);
        asignacion.setChofer(chofer);
        asignacion.setUnidad(unidad);
        asignacion.setFechaAsignacion(LocalDateTime.now());
        asignacion.setEstado(Asignacion.EstadoAsignacion.PENDIENTE);

        return asignacionRepository.save(asignacion);
    }

    public List<Asignacion> listarAsignaciones() {
        return asignacionRepository.findAll();
    }

    public Asignacion obtenerPorId(Integer id) {
        return asignacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
    }

    @Transactional
    public Asignacion actualizarEstado(Integer id, Asignacion.EstadoAsignacion nuevoEstado) {
        Asignacion asignacion = obtenerPorId(id);
        asignacion.setEstado(nuevoEstado);
        return asignacionRepository.save(asignacion);
    }

    public void eliminarAsignacion(Integer id) {
        Asignacion asignacion = obtenerPorId(id);
        asignacionRepository.delete(asignacion);
    }
}
