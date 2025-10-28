package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.dto.ClienteUpdateDTO;
import com.taxisaeropuerto.taxisAeropuerto.entity.Cliente;
import com.taxisaeropuerto.taxisAeropuerto.entity.Rol;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.ClienteRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.RolRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;
    private final EmailService emailService;

    public void registerUser(String nombre, String apellido, String telefono, String correo, String password) {
        if (userRepository.findByCorreo(correo).isPresent()) {
            throw new RuntimeException("El usuario ya existe.");
        }

        Rol rolCliente = rolRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

        // 1️⃣ Crear usuario
        User user = new User();
        user.setUsername(correo);
        user.setCorreo(correo);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(false);
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setRol(rolCliente);
        userRepository.save(user);

        // 2️⃣ Crear cliente asociado
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setTelefono(telefono);
        cliente.setUsuario(user);
        clienteRepository.save(cliente);

        // 3️⃣ Enviar correo de verificación
        emailService.sendClienteVerificationEmail(correo, user.getVerificationToken());
    }

    // 🔹 Obtener usuario por correo
    public User getUserByCorreo(String correo) {
        return userRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));
    }

    // 🔹 Obtener usuario por username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
    }

    // 🔹 Buscar usuario por username o correo
    public Optional<User> findByUsernameOrCorreo(String usernameOrCorreo) {
        return userRepository.findByUsernameOrCorreo(usernameOrCorreo, usernameOrCorreo);
    }

    // 🔹 Verificar usuario por token de verificación
    public void verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Token de verificación inválido."));
        user.setEnabled(true);
        user.setVerificationToken(null);
        userRepository.save(user);
    }

    // 🔹 Guardar usuario
    public User saveUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }
}
