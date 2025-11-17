package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.dto.AsignacionRequest;
import com.taxisaeropuerto.taxisAeropuerto.dto.AsignacionResponse;
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
    public AsignacionResponse crearAsignacion(AsignacionRequest dto) {

        // 1. Obtener la reserva
        Reserva reserva = reservaRepository.findById(dto.getIdReserva())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        // 2. Ver si ya existe asignación
        Asignacion asignacion = asignacionRepository.findByReserva(reserva)
                .orElse(null);

        // 3. Crear asignación si no existe
        if (asignacion == null) {
            asignacion = new Asignacion();
            asignacion.setReserva(reserva);
            asignacion.setFechaAsignacion(LocalDateTime.now());
            asignacion.setEstado(Asignacion.EstadoAsignacion.PENDIENTE);
        }

        // 4. Asignar unidad
        if (dto.getIdUnidad() != null) {
            Unidad unidad = unidadRepository.findById(dto.getIdUnidad())
                    .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));

            if (asignacionRepository.existsByUnidadAndEstadoIn(
                    unidad, List.of(Asignacion.EstadoAsignacion.PENDIENTE, Asignacion.EstadoAsignacion.ACTIVA))) {
                throw new RuntimeException("Unidad ya está ocupada");
            }

            asignacion.setUnidad(unidad);
            unidad.setEstado(Unidad.EstadoUnidad.OCUPADA);
            unidadRepository.save(unidad);
        }

        // 5. Asignar chofer
        if (dto.getIdChofer() != null) {
            Chofer chofer = choferRepository.findById(dto.getIdChofer())
                    .orElseThrow(() -> new RuntimeException("Chofer no encontrado"));

            if (asignacionRepository.existsByChoferAndEstadoIn(
                    chofer, List.of(Asignacion.EstadoAsignacion.PENDIENTE, Asignacion.EstadoAsignacion.ACTIVA))) {
                throw new RuntimeException("Chofer ya está ocupado");
            }

            asignacion.setChofer(chofer);
            chofer.setEstado(Chofer.EstadoChofer.OCUPADO);
            choferRepository.save(chofer);
        }

        // 6. Guardar asignación actualizada
        asignacionRepository.save(asignacion);

        // 7. Confirmar reserva
        reserva.setEstado(Reserva.EstadoReserva.CONFIRMADA);
        reservaRepository.save(reserva);

        // 🚫 8. ELIMINADO — Ya NO se genera PDF aquí
        // NO se guarda pdfPath
        // NO se maneja nombrePdf
        // Render NO admite guardar archivos en disco

        // 9. Respuesta limpia
        AsignacionResponse response = new AsignacionResponse();
        response.setIdAsignacion(asignacion.getIdAsignacion());
        response.setIdReserva(reserva.getIdReserva());
        response.setDestino(reserva.getDestino());
        response.setEstadoReserva(reserva.getEstado().name());
        response.setNombreCliente(reserva.getCliente().getNombre());
        response.setApellidoCliente(reserva.getCliente().getApellido());
        response.setNombreChofer(asignacion.getChofer() != null ? asignacion.getChofer().getNombre() : null);
        response.setApellidoChofer(asignacion.getChofer() != null ? asignacion.getChofer().getApellido() : null);
        response.setPlacaUnidad(asignacion.getUnidad() != null ? asignacion.getUnidad().getPlaca() : null);
        response.setFechaAsignacion(asignacion.getFechaAsignacion());
        response.setEstadoAsignacion(asignacion.getEstado().name());

        // ⚠️ YA NO HAY PDF AQUÍ
        response.setComprobantePdf(null);

        return response;
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
