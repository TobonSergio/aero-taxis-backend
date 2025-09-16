package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.*;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.service.AuthService;
import com.taxisaeropuerto.taxisAeropuerto.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication; // ✅ Import necesario
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            return new ResponseEntity<>(
                    "¡Usuario registrado! Por favor, verifica tu correo electrónico para activar tu cuenta.",
                    HttpStatus.CREATED
            );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Método de registro con correo electrónico
    @PostMapping("/register/email")
    public ResponseEntity<?> registerUserByEmail(@Valid @RequestBody RegisterByEmailRequest request) {
        try {
            userService.registerUserByEmail(request);
            return new ResponseEntity<>(
                    "¡Usuario registrado con correo! Por favor, verifica tu correo electrónico para activar tu cuenta.",
                    HttpStatus.CREATED
            );
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

    @PostMapping("/admin/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUserByAdmin(@Valid @RequestBody AdminCreateUserRequest request) {
        try {
            userService.createUserByAdmin(request);
            return new ResponseEntity<>(
                    "Usuario creado con éxito. Revisa tu correo para activar la cuenta.",
                    HttpStatus.CREATED
            );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
        @GetMapping("/me")
        public ResponseEntity<?> getCurrentUser(Authentication authentication) {
            try {
                String usernameOrEmail = authentication.getName();
                User user = userService.getUserByUsernameOrEmail(usernameOrEmail); // nuevo método

                return ResponseEntity.ok(Map.of(
                        "id", user.getId(),
                        "username", user.getUsername() != null ? user.getUsername() : "",
                        "name", user.getName() != null ? user.getName() : "",
                        "lastName", user.getLastName() != null ? user.getLastName() : "",
                        "email", user.getEmail(),
                        "number", user.getNumber() != null ? user.getNumber() : "",
                        "rolId", user.getRol() != null ? user.getRol().getId() : 0,
                        "rolName", user.getRol() != null ? user.getRol().getNombre() : "USER"
                ));
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of("error", e.getMessage())
                );
            }
        }


}
