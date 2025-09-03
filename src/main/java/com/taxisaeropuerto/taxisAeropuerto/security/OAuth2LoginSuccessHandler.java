package com.taxisaeropuerto.taxisAeropuerto.security;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    @Value("${custom.frontredirecturl}")
    private String frontendRedirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
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
                    return userRepository.save(newUser);
                });

        // Genera el token JWT
        String jwtToken = jwtService.generateToken(user);

        // 3. Redirección al frontend con el token en la URL
        String redirectUrl = frontendRedirectUrl + "?token=" + jwtToken;
        response.sendRedirect(redirectUrl);
    }
}