package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.dto.AuthResponse;
import com.taxisaeropuerto.taxisAeropuerto.dto.LoginRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.Rol;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.RolRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import com.taxisaeropuerto.taxisAeropuerto.service.EmailService;
import com.taxisaeropuerto.taxisAeropuerto.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthResponse login(LoginRequest request) {
        String usernameOrEmail = request.getUsername(); // puede ser username o correo
        String password = request.getPassword();

        // Buscar usuario por username O correo
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByCorreo(usernameOrEmail))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.getEnabled()) {
            throw new RuntimeException("Cuenta no verificada. Revisa tu correo para activar la cuenta.");
        }

        // Usar el username real del usuario para autenticar
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getCorreo(), password)
        );

        // Generar token JWTs
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

}
