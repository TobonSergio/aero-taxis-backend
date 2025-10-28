package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.dto.ClienteUpdateDTO;
import com.taxisaeropuerto.taxisAeropuerto.entity.Cliente;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.ClienteRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 🔹 Actualizar cliente
    public Cliente updateCliente(Long userId, ClienteUpdateDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Cliente cliente = clienteRepository.findByUsuario(user)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Actualizar datos del cliente
        if(request.getNombre() != null) cliente.setNombre(request.getNombre());
        if(request.getApellido() != null) cliente.setApellido(request.getApellido());
        if(request.getTelefono() != null) cliente.setTelefono(request.getTelefono());
        if(request.getDireccion() != null) cliente.setDireccion(request.getDireccion());
        if(request.getCiudad() != null) cliente.setCiudad(request.getCiudad());
        if(request.getIdioma() != null) cliente.setIdioma(request.getIdioma());
        if(request.getFechaNacimiento() != null) cliente.setFechaNacimiento(request.getFechaNacimiento());
        if(request.getGenero() != null) cliente.setGenero(request.getGenero());

        // Actualizar contraseña si viene
        if(request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);
        }

        return clienteRepository.save(cliente);
    }

    // 🔹 Obtener cliente por usuario
    public Cliente getClienteByUsuario(User user) {
        return clienteRepository.findByUsuario(user)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }
}
