package com.taxisaeropuerto.taxisAeropuerto.controller;

import com.taxisaeropuerto.taxisAeropuerto.entity.Cliente;
import com.taxisaeropuerto.taxisAeropuerto.entity.Rol;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.ClienteRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.RolRepository;
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
    private final ClienteRepository clienteRepository;
    private final RolRepository rolRepository;
    private final JwtService jwtService;

    @Value("${custom.frontredirecturl}")
    private String frontendRedirectUrl;

    @GetMapping("/oauth2-success")
    public RedirectView handleOAuth2Success(@AuthenticationPrincipal OAuth2User oauth2User) {

        String email = oauth2User.getAttribute("email");
        String fullName = oauth2User.getAttribute("name");

        // Separar nombre y apellido automáticamente
        String nombre = fullName != null ? fullName.split(" ")[0] : "Usuario";
        String apellido = fullName != null && fullName.contains(" ") ?
                fullName.substring(fullName.indexOf(" ") + 1) : "";

        // 1️⃣ Buscar usuario por correo
        User user = userRepository.findByCorreo(email).orElse(null);

        // 2️⃣ Si no existe, crearlo como CLIENTE
        if (user == null) {

            Rol rolCliente = rolRepository.findById(3L)
                    .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

            user = new User();
            user.setUsername(email);
            user.setCorreo(email);
            user.setEnabled(true);
            user.setPassword(null); // Usuario de Google no tiene contraseña
            user.setRol(rolCliente);
            userRepository.save(user);

            // 3️⃣ Crear Cliente asociado
            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setCorreo(email);      // IMPORTANTE (evita null en columna unique)
            cliente.setTelefono("");       // Google no lo envía
            cliente.setDireccion("");
            cliente.setCiudad("");
            cliente.setIdioma("es");
            cliente.setFechaNacimiento("");
            cliente.setGenero("");
            cliente.setUsuario(user);

            clienteRepository.save(cliente);
        }

        // 4️⃣ Generar JWT
        String jwtToken = jwtService.generateToken(user);

        // 5️⃣ Redirigir al frontend
        String redirectUrl = frontendRedirectUrl + "?token=" + jwtToken + "&type=usuario";
        return new RedirectView(redirectUrl);
    }
}
