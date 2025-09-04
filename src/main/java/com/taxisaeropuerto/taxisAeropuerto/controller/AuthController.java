package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.AuthResponse;
import com.taxisaeropuerto.taxisAeropuerto.dto.LoginRequest;
import com.taxisaeropuerto.taxisAeropuerto.dto.RegisterByEmailRequest;
import com.taxisaeropuerto.taxisAeropuerto.dto.RegisterRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.service.AuthService;
import com.taxisaeropuerto.taxisAeropuerto.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping; // <-- Nueva importación
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; // <-- Nueva importación
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        System.out.println("Login request received: " + request.getUsername());
        return ResponseEntity.ok(authService.login(request));
    }

    // Método de registro
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest request) {
        try {
            User registeredUser = userService.registerUser(request);
            // Devuelve un mensaje en lugar de todo el objeto de usuario
            return new ResponseEntity<>("¡Usuario registrado! Por favor, verifica tu correo electrónico para activar tu cuenta.", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Método de registro con correo electrónico
    @PostMapping("/register/email")
    public ResponseEntity<?> registerUserByEmail(@Valid @RequestBody RegisterByEmailRequest request) {
        try {
            userService.registerUserByEmail(request);
            return new ResponseEntity<>("¡Usuario registrado con correo! Por favor, verifica tu correo electrónico para activar tu cuenta.", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("token") String token) {
        try {
            userService.verifyUser(token);

            return ResponseEntity.ok().body(
                    Map.of(
                            "status", "success",
                            "message", "¡Tu cuenta ha sido verificada con éxito! Ya puedes iniciar sesión."
                    )
            );

        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", "error",
                            "message", ex.getMessage()
                    )
            );
        }
    }
}