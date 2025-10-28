package com.taxisaeropuerto.taxisAeropuerto.security;

import com.taxisaeropuerto.taxisAeropuerto.entity.Rol;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.RolRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
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

    @Value("${custom.frontredirecturl}")
    private String frontendRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // 🔹 Buscar usuario existente por correo
        Optional<User> optionalUser = userRepository.findByCorreo(email);
        User user;

        if (optionalUser.isPresent()) {
            // ✅ Ya existe → lo usamos directamente
            user = optionalUser.get();

        } else {
            // 🆕 No existe → lo creamos con rol CLIENTE (rolId = 3)
            Rol rolCliente = rolRepository.findById(3L)
                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE (ID 3) no encontrado"));

            user = new User();
            user.setUsername(email);
            user.setCorreo(email);
            user.setPassword(null); // no usa contraseña (Google)
            user.setEnabled(true);
            user.setVerificationToken(UUID.randomUUID().toString());
            user.setRol(rolCliente);

            userRepository.save(user);
        }

        // 🔹 Generar token JWT
        String jwtToken = jwtService.generateToken(user);

        // 🔹 Redirigir al frontend con el token
        String redirectUrl = frontendRedirectUrl + "?token=" + jwtToken;
        response.sendRedirect(redirectUrl);
    }
}
