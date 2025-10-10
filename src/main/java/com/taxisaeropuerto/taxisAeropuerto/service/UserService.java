package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.dto.AdminCreateUserRequest;
import com.taxisaeropuerto.taxisAeropuerto.dto.RegisterByEmailRequest;
import com.taxisaeropuerto.taxisAeropuerto.dto.RegisterRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.Rol;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.RolRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User registerUser(RegisterRequest request) {
        // Validación de duplicados
        if (userRepository.findByUsername(request.getUsername()).isPresent() ||
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El usuario o correo ya existen.");
        }

        // Creación del nuevo usuario
        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setNumber(request.getNumber());
        newUser.setUsername(request.getUsername());

        // Encripta la contraseña antes de guardarla
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        // Asigna el rol por defecto buscándolo por su ID
        Rol userRol = rolRepository.findById(3L)
                .orElseThrow(() -> new NoSuchElementException("El rol con ID '3' no se encontró."));
        newUser.setRol(userRol);

        // --- Nuevos pasos para la verificación de email ---
        newUser.setEnabled(false); // La cuenta está inactiva por defecto
        newUser.setVerificationToken(UUID.randomUUID().toString()); // Se genera el token único

        // Guarda el usuario en la base de datos
        User savedUser = userRepository.save(newUser);

        // Envía el correo de verificación
        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());

        return savedUser;
    }

    // --- Nuevo método para verificar el usuario ---
    public void verifyUser(String token) {
        // 1. Busca el usuario por el token
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Token de verificación inválido."));

        // 2. Activa la cuenta
        user.setEnabled(true);

        // 3. Limpia el token para que no pueda ser usado de nuevo
        user.setVerificationToken(null);

        // 4. Guarda los cambios en la base de datos
        userRepository.save(user);
    }

    public User registerUserByEmail(RegisterByEmailRequest request) {
        // 1. Validar que el correo no exista
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        // 2. Crear el nuevo usuario
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setUsername(request.getEmail()); // Se usa el correo como nombre de usuario
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        // Asigna los campos opcionales como nulos para la base de datos
        newUser.setName(null);
        newUser.setLastName(null);
        newUser.setNumber(null);

        // 3. Asignar el rol por defecto
        Rol userRol = rolRepository.findById(3L)
                .orElseThrow(() -> new NoSuchElementException("El rol con ID '3' no se encontró."));
        newUser.setRol(userRol);

        // 4. Configurar para verificación
        newUser.setEnabled(false);
        newUser.setVerificationToken(UUID.randomUUID().toString());

        // 5. Guardar y enviar correo
        User savedUser = userRepository.save(newUser);
        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());

        return savedUser;
    }

    public User createUserByAdmin(AdminCreateUserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent() ||
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El usuario o correo ya existen.");
        }

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setLastName(request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setNumber(request.getNumber());
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RuntimeException("El rol con ID " + request.getRolId() + " no existe."));
        newUser.setRol(rol);

        // ✅ Igual que en registerUser
        newUser.setEnabled(false);
        newUser.setVerificationToken(UUID.randomUUID().toString());

        User savedUser = userRepository.save(newUser);

        // ✅ Enviar correo
        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());

        return savedUser;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public User getUserByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    public void save(User user) {
        userRepository.save(user);
    }
}