package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.dto.ChoferCreateRequest;
import com.taxisaeropuerto.taxisAeropuerto.dto.ChoferResponse;
import com.taxisaeropuerto.taxisAeropuerto.entity.Chofer;
import com.taxisaeropuerto.taxisAeropuerto.entity.Rol;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.ChoferRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.RolRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChoferService {

    private final ChoferRepository choferRepository;
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;



    // 🔹 Crear chofer
    public Chofer crearChofer(ChoferCreateRequest request) {
        if (userRepository.existsByCorreo(request.getCorreo()) || userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El usuario o correo ya están registrados");
        }

        Rol rolChofer = rolRepository.findById(4L)
                .orElseThrow(() -> new RuntimeException("Rol CHOFER no encontrado"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setCorreo(request.getCorreo());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setRol(rolChofer);
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        userRepository.save(user);

        Chofer chofer = new Chofer();
        chofer.setNombre(request.getNombre());
        chofer.setApellido(request.getApellido());
        chofer.setCorreo(request.getCorreo());
        chofer.setTelefono(request.getTelefono());
        chofer.setLicenciaConduccion(request.getLicenciaConduccion());
        chofer.setBilingue(request.getBilingue());
        chofer.setEstado(Chofer.EstadoChofer.DISPONIBLE); // ✅ Estado por defecto
        chofer.setUsuario(user);
        choferRepository.save(chofer);

        //emailService.sendUsuarioVerificationEmail(user.getCorreo(), token);

        return chofer;
    }


    public List<ChoferResponse> listarChoferesDisponibles() {
        return choferRepository.findByEstado(Chofer.EstadoChofer.DISPONIBLE)
                .stream()
                .map(c -> new ChoferResponse(
                        c.getIdChofer(),
                        c.getNombre(),
                        c.getApellido(),
                        c.getCorreo(),
                        c.getTelefono(),
                        c.getLicenciaConduccion(),
                        c.getBilingue(),
                        c.getEstado().name(),
                        c.getUsuario() != null ? c.getUsuario().getUsername() : null,
                        c.getUsuario() != null && c.getUsuario().getRol() != null
                                ? c.getUsuario().getRol().getNombre()
                                : null
                ))
                .collect(Collectors.toList());
    }

    // Listar todos los choferes
    public List<Chofer> listarChoferes() {
        return choferRepository.findAll();
    }

    // Obtener chofer por ID
    public Chofer obtenerChoferPorId(Integer id) {
        return choferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chofer no encontrado"));
    }

    // Actualizar chofer
    public Chofer actualizarChofer(Integer id, ChoferCreateRequest request) {
        Chofer chofer = obtenerChoferPorId(id);
        User usuario = chofer.getUsuario();

        usuario.setUsername(request.getUsername());
        usuario.setCorreo(request.getCorreo());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(usuario);

        chofer.setNombre(request.getNombre());
        chofer.setApellido(request.getApellido());
        chofer.setCorreo(request.getCorreo());
        chofer.setTelefono(request.getTelefono());
        chofer.setLicenciaConduccion(request.getLicenciaConduccion());
        chofer.setBilingue(request.getBilingue());

        return choferRepository.save(chofer);
    }

    // Eliminar chofer
    public void eliminarChofer(Integer id) {
        Chofer chofer = obtenerChoferPorId(id);
        User usuario = chofer.getUsuario();

        choferRepository.delete(chofer);
        if (usuario != null) {
            userRepository.delete(usuario);
        }
    }
}
