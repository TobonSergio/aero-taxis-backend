package com.taxisaeropuerto.taxisAeropuerto.service;

import com.google.zxing.WriterException;
import com.taxisaeropuerto.taxisAeropuerto.dto.*;
import com.taxisaeropuerto.taxisAeropuerto.entity.Cliente;
import com.taxisaeropuerto.taxisAeropuerto.entity.Reserva;
import com.taxisaeropuerto.taxisAeropuerto.entity.Ruta;
import com.taxisaeropuerto.taxisAeropuerto.entity.Staff;
import com.taxisaeropuerto.taxisAeropuerto.repository.ClienteRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.ReservaRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.RutaRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final RutaRepository rutaRepository;
    private final StaffRepository staffRepository;

    public ReservaService(ReservaRepository reservaRepository,
                          ClienteRepository clienteRepository,
                          RutaRepository rutaRepository,
                          StaffRepository staffRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.rutaRepository = rutaRepository;
        this.staffRepository = staffRepository;
    }

    @Transactional
    public ReservaResponse crearReserva(ReservaRequest dto) {
        // 1️⃣ Obtener cliente
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // 2️⃣ Obtener ruta
        Ruta ruta = rutaRepository.findById(dto.getIdRuta())
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));

        // 3️⃣ Obtener staff si existe
        Staff staff = null;
        if (dto.getIdStaff() != null) {
            staff = staffRepository.findById(dto.getIdStaff())
                    .orElseThrow(() -> new RuntimeException("Staff no encontrado"));
        }

        // 4️⃣ Crear la reserva
        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setStaff(staff);
        reserva.setRuta(ruta);
        reserva.setLugarRecogida(dto.getLugarRecogida());
        reserva.setDestino(dto.getDestino());
        reserva.setFechaReserva(dto.getFechaReserva());
        reserva.setHoraReserva(dto.getHoraReserva());
        reserva.setComentarios(dto.getComentarios());
        reserva.setEstado(Reserva.EstadoReserva.PENDIENTE);

        // 5️⃣ Guardar la reserva
        reserva = reservaRepository.save(reserva);

        // 6️⃣ Construir y devolver la respuesta
        ReservaResponse reservaResponse = new ReservaResponse();
        reservaResponse.setIdReserva(reserva.getIdReserva());
        reservaResponse.setIdCliente(reserva.getCliente().getIdCliente());
        reservaResponse.setNombre(reserva.getCliente().getNombre());
        reservaResponse.setApellido(reserva.getCliente().getApellido());
        reservaResponse.setIdRuta(reserva.getRuta().getId());
        reservaResponse.setEmail(reserva.getCliente().getCorreo());
        reservaResponse.setTelefono(reserva.getCliente().getTelefono());
        reservaResponse.setDireccion(reserva.getCliente().getDireccion());
        reservaResponse.setComentarios(reserva.getComentarios());

        return reservaResponse;
    }


    public List<ReservaResponse> listarReservas() {
        List<Reserva> reservas = reservaRepository.findAll();

        return reservas.stream()
                .map(reserva -> new ReservaResponse(
                        reserva.getIdReserva(),
                        reserva.getCliente().getIdCliente(),
                        reserva.getRuta().getId(),
                        reserva.getCliente().getNombre(),
                        reserva.getCliente().getApellido(),
                        reserva.getCliente().getDireccion(),
                        reserva.getCliente().getTelefono(),
                        reserva.getCliente().getUsuario().getCorreo(),
                        reserva.getFechaReserva(),
                        reserva.getComentarios()
                ))
                .toList();
    }


    // 🔹 Obtener reserva por ID
    public Reserva obtenerReservaPorId(Integer id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    // 🔹 Actualizar reserva
    @Transactional
    public Reserva actualizarReserva(Integer id, ReservaRequest dto) {
        Reserva reserva = obtenerReservaPorId(id);

        reserva.setLugarRecogida(dto.getLugarRecogida());
        reserva.setDestino(dto.getDestino());
        reserva.setFechaReserva(dto.getFechaReserva());
        reserva.setHoraReserva(dto.getHoraReserva());
        reserva.setComentarios(dto.getComentarios());

        // Actualizar staff si viene en el DTO
        if (dto.getIdStaff() != null) {
            Staff staff = staffRepository.findById(dto.getIdStaff())
                    .orElseThrow(() -> new RuntimeException("Staff no encontrado"));
            reserva.setStaff(staff);
        }

        return reservaRepository.save(reserva);
    }

    // 🔹 Eliminar reserva
    public void eliminarReserva(Integer id) {
        Reserva reserva = obtenerReservaPorId(id);
        reservaRepository.delete(reserva);
    }

    public List<ReservaListClienteResponse> listarReservasPorCliente(Integer idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<Reserva> reservas = reservaRepository.findByCliente(cliente);

        return reservas.stream().map(reserva -> {
            ReservaListClienteResponse dto = new ReservaListClienteResponse();
            dto.setIdReserva(reserva.getIdReserva());
            dto.setLugarRecogida(reserva.getLugarRecogida());
            dto.setDestino(reserva.getDestino());
            dto.setFechaReserva(reserva.getFechaReserva());
            dto.setHoraReserva(reserva.getHoraReserva());
            dto.setEstado(reserva.getEstado().name());
            dto.setComentarios(reserva.getComentarios());
            if (reserva.getRuta() != null) {
                dto.setNombreRuta(reserva.getRuta().getFin());
            }
            return dto;
        }).toList();
    }


    public List<Reserva> listarReservasPorEstado(Reserva.EstadoReserva estado) {
        return reservaRepository.findByEstado(estado);
    }

    public List<ReservaPendienteResponse> listarReservasPendientes() {
        List<Reserva> reservas = reservaRepository.findByEstado(Reserva.EstadoReserva.PENDIENTE);

        return reservas.stream().map(reserva -> {
            ReservaPendienteResponse dto = new ReservaPendienteResponse();
            dto.setIdReserva(reserva.getIdReserva());
            dto.setFechaReserva(reserva.getFechaReserva().toString());
            dto.setEstado(reserva.getEstado().name());
            dto.setNombreCliente(reserva.getCliente().getNombre());
            dto.setApellidoCliente(reserva.getCliente().getApellido());
            dto.setTelefonoCliente(reserva.getCliente().getTelefono());
            dto.setCiudadCliente(reserva.getCliente().getCiudad());
            if (reserva.getRuta() != null) {
                dto.setOrigen(reserva.getRuta().getInicio());
                dto.setDestino(reserva.getRuta().getFin());
            }
            return dto;
        }).toList();
    }

    // Dentro de la clase ReservaService:
    public List<ReservaDashboardResponse> listarReservasDashboard() {
        List<Reserva> reservas = reservaRepository.findAll();

        return reservas.stream().map(reserva -> {
            ReservaDashboardResponse dto = new ReservaDashboardResponse();
            dto.setIdReserva(reserva.getIdReserva());

            // Nombre del cliente (si existe)
            if (reserva.getCliente() != null) {
                dto.setNombreCliente(reserva.getCliente().getNombre());
            } else {
                dto.setNombreCliente(null);
            }

            // Destino: priorizamos el campo destino de la reserva; si no existe usamos la ruta (fin)
            String destino = reserva.getDestino();
            if ((destino == null || destino.isBlank()) && reserva.getRuta() != null) {
                destino = reserva.getRuta().getFin();
            }
            dto.setDestino(destino);

            // Fecha de reserva (LocalDateTime en el DTO)
            dto.setFechaReserva(reserva.getFechaReserva());

            // Estado como texto
            dto.setEstado(reserva.getEstado() != null ? reserva.getEstado().name() : null);

            return dto;
        }).collect(Collectors.toList());
    }

}
