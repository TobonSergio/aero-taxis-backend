package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.dto.ChoferCreateRequest;
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
        // Validar usuario existente
        if (userRepository.existsByCorreo(request.getCorreo()) || userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El usuario o correo ya están registrados");
        }

        // Obtener rol CHOFER
        Rol rolChofer = rolRepository.findById(4L)
                .orElseThrow(() -> new RuntimeException("Rol CHOFER no encontrado"));

        // Crear usuario
        User user = new User();
        user.setUsername(request.getUsername());
        user.setCorreo(request.getCorreo());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);
        user.setRol(rolChofer);
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        userRepository.save(user);

        // Crear chofer
        Chofer chofer = new Chofer();
        chofer.setNombre(request.getNombre());
        chofer.setApellido(request.getApellido());
        chofer.setCorreo(request.getCorreo());
        chofer.setTelefono(request.getTelefono());
        chofer.setLicenciaConduccion(request.getLicenciaConduccion());
        chofer.setBilingue(request.getBilingue());
        chofer.setUsuario(user);
        choferRepository.save(chofer);

        // Enviar correo de verificación
        emailService.sendUsuarioVerificationEmail(user.getCorreo(), token);

        return chofer;
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
