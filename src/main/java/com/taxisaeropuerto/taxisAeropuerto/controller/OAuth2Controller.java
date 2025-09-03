package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import com.taxisaeropuerto.taxisAeropuerto.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @GetMapping("/oauth2-success")
    public RedirectView handleOAuth2Success(@AuthenticationPrincipal OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // 1. Lógica de negocio: Busca o crea el usuario
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    // Los campos de usuario y contraseña son nulos para OAuth
                    newUser.setUsername(null);
                    newUser.setPassword(null);
                    // Asigna un rol por defecto
                    return userRepository.save(newUser);
                });

        // 2. Generación del JWT
        // ✅ Corregido: Pasamos el objeto 'user' completo
        String jwtToken = jwtService.generateToken(user);

        // 3. Redirección al frontend con el token en la URL
        String frontendUrl = "http://localhost:3000/oauth-callback?token=" + jwtToken;

        return new RedirectView(frontendUrl);
    }
}