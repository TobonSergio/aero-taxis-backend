package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.AuthResponse;
import com.taxisaeropuerto.taxisAeropuerto.dto.ClienteRegisterRequest;
import com.taxisaeropuerto.taxisAeropuerto.dto.LoginRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.service.AuthService;
import com.taxisaeropuerto.taxisAeropuerto.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    // 🔹 Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ClienteRegisterRequest request) {
        try {
            userService.registerUser(
                    request.getNombre(),
                    request.getApellido(),
                    request.getTelefono(),
                    request.getCorreo(),
                    request.getPassword()
            );

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Usuario registrado, revisa tu correo para verificar."
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }


    // 🔹 Verificación de correo
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("token") String token) {
        try {
            userService.verifyUser(token);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "¡Tu cuenta ha sido verificada con éxito!"
            ));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", ex.getMessage()
            ));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            String correo = authentication.getName();
            User user = userService.getUserByCorreo(correo);

            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("correo", user.getCorreo());
            response.put("rolId", user.getRol() != null ? user.getRol().getRolId() : null);
            response.put("rolName", user.getRol() != null ? user.getRol().getNombre() : "USER");

            if (user.getCliente() != null) {
                response.put("idCliente", user.getCliente().getIdCliente());
            } else if (user.getStaff() != null) {
                response.put("idStaff", user.getStaff().getIdStaff());
            } else if (user.getChofer() != null) {
                response.put("idChofer", user.getChofer().getIdChofer());
            }

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

}

