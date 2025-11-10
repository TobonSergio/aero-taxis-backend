package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.dto.ClienteProfileResponse;
import com.taxisaeropuerto.taxisAeropuerto.dto.ClienteUpdateDTO;
import com.taxisaeropuerto.taxisAeropuerto.entity.Cliente;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.service.ClienteService;
import com.taxisaeropuerto.taxisAeropuerto.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
    @RequestMapping("/api/clientes")
    @RequiredArgsConstructor
    public class ClienteController {

        private final ClienteService clienteService;
        private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);
            Cliente cliente = clienteService.getClienteByUsuario(user);

            ClienteProfileResponse response = new ClienteProfileResponse();
            response.setIdCliente(cliente.getIdCliente());
            response.setNombre(cliente.getNombre());
            response.setApellido(cliente.getApellido());
            response.setCorreo(cliente.getCorreo() != null ? cliente.getCorreo() : user.getCorreo());
            response.setTelefono(cliente.getTelefono());
            response.setDireccion(cliente.getDireccion());
            response.setCiudad(cliente.getCiudad());
            response.setIdioma(cliente.getIdioma());
            response.setFechaNacimiento(
                    cliente.getFechaNacimiento() != null ? cliente.getFechaNacimiento().toString() : null
            );
            response.setGenero(cliente.getGenero());
            response.setIdUsuario(user.getId());
            response.setUsername(user.getUsername());
            response.setRolName(user.getRol().getNombre());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }


    // 🔹 Actualizar datos del cliente actual
    @PutMapping("/me")
    public ResponseEntity<?> updateMe(Authentication authentication,
                                      @RequestBody ClienteUpdateDTO request) {
        try {
            String username = authentication.getName();
            User user = userService.getUserByUsername(username);
            Cliente updatedCliente = clienteService.updateCliente(user.getId(), request);
            return ResponseEntity.ok(updatedCliente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
}
