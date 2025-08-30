package com.taxisaeropuerto.taxisAeropuerto.util;

import com.taxisaeropuerto.taxisAeropuerto.entity.Rol;
import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.RolRepository;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

    public UserInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RolRepository rolRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        String username = "tobon2";
        String rawPassword = "tobon*2";

        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPassword));

            Rol rol = rolRepository.findByNombre("admin")
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            user.setRol(rol);

            userRepository.save(user);
            System.out.println("Usuario creado: " + username);
        } else {
            System.out.println("El usuario ya existe: " + username);
        }
    }
}

