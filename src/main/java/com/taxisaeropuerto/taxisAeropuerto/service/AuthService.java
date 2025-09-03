package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.dto.AuthResponse;
import com.taxisaeropuerto.taxisAeropuerto.dto.LoginRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.User; // Importa la clase User
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository; // Importa el repositorio
import com.taxisaeropuerto.taxisAeropuerto.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository; // <-- Se agrega el repositorio

    public AuthResponse login(LoginRequest request) {
        System.out.println("Autenticando usuario: " + request.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            System.out.println("Autenticación exitosa");
        } catch (Exception e) {
            System.out.println("Error de autenticación: " + e.getMessage());
            throw e;
        }

        // Después de una autenticación exitosa, busca el usuario en el repositorio
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(user);
        System.out.println("Token generado: " + token);
        return new AuthResponse(token);
    }

}