package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.dto.AuthResponse;
import com.taxisaeropuerto.taxisAeropuerto.dto.LoginRequest;
import com.taxisaeropuerto.taxisAeropuerto.security.CustomUserDetailsService;
import com.taxisaeropuerto.taxisAeropuerto.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public AuthResponse login(LoginRequest request) {
        System.out.println("Autenticando usuario: " + request.getUsername());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            System.out.println("Autenticación exitosa");
        } catch (Exception e) {
            System.out.println("Error de autenticación: " + e.getMessage());
            throw e; // para que Spring devuelva error 401
        }

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(user);
        System.out.println("Token generado: " + token);
        return new AuthResponse(token);
    }

}