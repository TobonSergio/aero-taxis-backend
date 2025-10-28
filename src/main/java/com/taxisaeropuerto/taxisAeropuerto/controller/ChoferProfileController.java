package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.ChoferCreateRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.Chofer;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.ChoferRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chofer")
@RequiredArgsConstructor
public class ChoferProfileController {

    private final ChoferRepository choferRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    @PreAuthorize("hasRole('CHOFER')")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String correo = authentication.getName(); // correo del JWT
        Chofer chofer = choferRepository.findByUsuario_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Chofer no encontrado"));
        return ResponseEntity.ok(chofer);
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CHOFER')")
    public ResponseEntity<?> updateProfile(Authentication authentication,
                                           @RequestBody ChoferCreateRequest request) {
        String correo = authentication.getName(); // correo del JWT
        Chofer chofer = choferRepository.findByUsuario_Correo(correo)
                .orElseThrow(() -> new RuntimeException("Chofer no encontrado"));

        // Actualizar datos del usuario asociado
        User usuario = chofer.getUsuario();
        usuario.setUsername(request.getUsername());
        usuario.setCorreo(request.getCorreo());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(usuario);

        // Actualizar datos del chofer
        chofer.setNombre(request.getNombre());
        chofer.setApellido(request.getApellido());
        chofer.setTelefono(request.getTelefono());
        chofer.setLicenciaConduccion(request.getLicenciaConduccion());
        chofer.setBilingue(request.getBilingue());
        choferRepository.save(chofer);

        return ResponseEntity.ok(chofer);
    }
}
