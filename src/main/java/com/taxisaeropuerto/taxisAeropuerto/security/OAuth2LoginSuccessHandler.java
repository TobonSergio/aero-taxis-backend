package com.taxisaeropuerto.taxisAeropuerto.security;

import com.taxisaeropuerto.taxisAeropuerto.entity.Rol;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final ClienteRepository clienteRepository;
    private final StaffRepository staffRepository;
    private final ChoferRepository choferRepository;

    @Value("${custom.frontredirecturl}")
    private String frontendRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");

        // 🔹 Buscar o crear usuario
        User user = userRepository.findByCorreo(email).orElseGet(() -> {
            Rol rolCliente = rolRepository.findById(3L)
                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE (ID 3) no encontrado"));
            User newUser = new User();
            newUser.setUsername(email);
            newUser.setCorreo(email);
            newUser.setPassword(null);
            newUser.setEnabled(true);
            newUser.setVerificationToken(UUID.randomUUID().toString());
            newUser.setRol(rolCliente);
            return userRepository.save(newUser);
        });

        // 🔹 Obtener idPerfil según el rol
        Long idPerfil = null;
        String rolNombre = user.getRol() != null ? user.getRol().getNombre() : "USER";
        switch (rolNombre.toUpperCase()) {
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

        // 🔹 Generar token JWT
        String jwtToken = jwtService.generateToken(user);

        // 🔹 Redirigir al frontend con todos los datos
        String redirectUrl = frontendRedirectUrl +
                "?token=" + jwtToken +
                "&id=" + user.getId() +
                "&correo=" + user.getCorreo() +
                "&username=" + user.getUsername() +
                "&rolid=" + (user.getRol() != null ? user.getRol().getRolId() : "") +
                "&rolnombre=" + rolNombre +
                "&idPerfil=" + (idPerfil != null ? idPerfil : "");
        response.sendRedirect(redirectUrl);
    }
}

