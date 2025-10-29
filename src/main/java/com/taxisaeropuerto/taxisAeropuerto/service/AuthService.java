package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.dto.AuthResponse;
import com.taxisaeropuerto.taxisAeropuerto.dto.LoginRequest;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.entity.Cliente;
import com.taxisaeropuerto.taxisAeropuerto.entity.Staff;
import com.taxisaeropuerto.taxisAeropuerto.entity.Chofer;
import com.taxisaeropuerto.taxisAeropuerto.repository.ClienteRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.StaffRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.ChoferRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ClienteRepository clienteRepository;
    private final StaffRepository staffRepository;
    private final ChoferRepository choferRepository;

    public AuthResponse login(LoginRequest request) {
        String usernameOrEmail = request.getUsername();
        String password = request.getPassword();

        // Buscar usuario por username o correo
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByCorreo(usernameOrEmail))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!user.getEnabled()) {
            throw new RuntimeException("Cuenta no verificada. Revisa tu correo para activar la cuenta.");
        }

        // Autenticar
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getCorreo(), password)
        );

        // Generar token
        String token = jwtService.generateToken(user);

        // 🔹 Buscar el perfil asociado según el rol
        Long idPerfil = null;

        switch (user.getRol().getNombre().toUpperCase()) {
            case "CLIENTE":
                idPerfil = clienteRepository.findByUsuario(user)
                        .map(cliente -> cliente.getIdCliente().longValue())
                        .orElse(null);
                break;

            case "STAFF":
                idPerfil = staffRepository.findByUsuario(user)
                        .map(staff -> staff.getIdStaff().longValue())
                        .orElse(null);
                break;

            case "CHOFER":
                idPerfil = choferRepository.findByUsuario(user)
                        .map(chofer -> chofer.getIdChofer().longValue())
                        .orElse(null);
                break;
        }

        // 🔹 Respuesta completa
        return new AuthResponse(
                token,
                user.getId(),
                user.getRol().getNombre(),
                idPerfil
        );
    }
}
