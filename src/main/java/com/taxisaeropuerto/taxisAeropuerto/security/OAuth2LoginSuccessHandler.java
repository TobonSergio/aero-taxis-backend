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

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RolRepository rolRepository; // ✅ Repositorio de roles

    @Value("${custom.frontredirecturl}")
    private String frontendRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // Busca o crea el usuario
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setUsername(null);
                    newUser.setPassword(null);
                    newUser.setEnabled(true); // Puedes habilitarlo automáticamente

                    // ✅ Asignar rol por defecto "CLIENTE"
                    Rol rolCliente = rolRepository.findByNombre("CLIENTE")
                            .orElseThrow(() -> new RuntimeException("El rol CLIENTE no existe en la base de datos"));
                    newUser.setRol(rolCliente);

                    return userRepository.save(newUser);
                });

        // ✅ Si ya existe pero no tiene rol, se lo asignamos también
        if (user.getRol() == null) {
            Rol rolCliente = rolRepository.findByNombre("CLIENTE")
                    .orElseThrow(() -> new RuntimeException("El rol CLIENTE no existe en la base de datos"));
            user.setRol(rolCliente);
            userRepository.save(user);
        }

        // Generar token JWT
        String jwtToken = jwtService.generateToken(user);

        // Redirección al frontend con el token
        String redirectUrl = frontendRedirectUrl + "?token=" + jwtToken;
        response.sendRedirect(redirectUrl);
    }
}
