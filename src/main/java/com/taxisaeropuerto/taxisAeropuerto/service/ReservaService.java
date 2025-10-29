package com.taxisaeropuerto.taxisAeropuerto.service;

import com.google.zxing.WriterException;
import com.taxisaeropuerto.taxisAeropuerto.dto.ReservaRequest;
import com.taxisaeropuerto.taxisAeropuerto.dto.ReservaResponse;
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

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final RutaRepository rutaRepository;
    private final StaffRepository staffRepository;
    private final QRGenerator qrGenerator;
    private final PDFService pdfService;

    public ReservaService(ReservaRepository reservaRepository,
                          ClienteRepository clienteRepository,
                          RutaRepository rutaRepository,
                          StaffRepository staffRepository,
                          QRGenerator qrGenerator,
                          PDFService pdfService) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.rutaRepository = rutaRepository;
        this.staffRepository = staffRepository;
        this.qrGenerator = qrGenerator;
        this.pdfService = pdfService;
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
        reserva.setStaff(staff); // puede ser null
        reserva.setRuta(ruta);
        reserva.setLugarRecogida(dto.getLugarRecogida());
        reserva.setDestino(dto.getDestino());
        reserva.setFechaReserva(dto.getFechaReserva());
        reserva.setHoraReserva(dto.getHoraReserva());
        reserva.setComentarios(dto.getComentarios());
        reserva.setEstado(Reserva.EstadoReserva.PENDIENTE);


        // 5️⃣ Guardar primero para generar ID
        reserva = reservaRepository.save(reserva);

        // 6️⃣ Generar QR y PDF dentro de try-catch para manejar excepciones
        try {
            String qrCodePath = qrGenerator.generarQR(reserva);
            reserva.setQrCode(qrCodePath);

            String pdfPath = pdfService.generarComprobante(reserva);
            reserva.setComprobantePdf(pdfPath);
        } catch (WriterException | IOException e) {
            // Manejo de errores: lanzamos RuntimeException para que Spring lo capture
            throw new RuntimeException("Error al generar QR o PDF", e);
        }

        var reservaEntity = reservaRepository.save(reserva);

        ReservaResponse reservaResponse = new ReservaResponse();
        reservaResponse.setIdReserva(reservaEntity.getId_reserva());
        reservaResponse.setIdCliente(reservaEntity.getCliente().getIdCliente());
        reservaResponse.setNombre(reservaEntity.getCliente().getNombre());
        reservaResponse.setApellido(reservaEntity.getCliente().getApellido());
        reservaResponse.setIdRuta(reservaEntity.getRuta().getId());
        reservaResponse.setEmail(reservaEntity.getCliente().getCorreo());
        reservaResponse.setTelefono(reservaEntity.getCliente().getTelefono());
        reservaResponse.setDireccion(reservaEntity.getCliente().getDireccion());
        reservaResponse.setComentarios(reservaEntity.getComentarios());
        // 7️⃣ Guardar nuevamente con QR y PDF
        return reservaResponse;
    }

    // 🔹 Listar todas las reservas
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
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

    public List<Reserva> listarReservasPorCliente(Integer idCliente) {
        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return reservaRepository.findByCliente(cliente);
    }

    public List<Reserva> listarReservasPorEstado(Reserva.EstadoReserva estado) {
        return reservaRepository.findByEstado(estado);
    }

}
