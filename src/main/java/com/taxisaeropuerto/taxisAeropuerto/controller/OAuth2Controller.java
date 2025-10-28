package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import com.taxisaeropuerto.taxisAeropuerto.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${custom.frontredirecturl}")
    private String frontendRedirectUrl;

    @GetMapping("/oauth2-success")
    public RedirectView handleOAuth2Success(@AuthenticationPrincipal OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");

        // Buscar si ya existe el usuario
        User user = userRepository.findByCorreo(email).orElse(null);

        // Si no existe, lo creamos
        if (user == null) {
            user = new User();
            user.setUsername(email); // puedes usar el email como username
            user.setEnabled(true);
            userRepository.save(user);
        }

        // Generar el token JWT
        String jwtToken = jwtService.generateToken(user);

        // Redirigir al frontend con el token
        String redirectUrl = frontendRedirectUrl + "?token=" + jwtToken + "&type=usuario";
        return new RedirectView(redirectUrl);
    }
}
