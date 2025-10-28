package com.taxisaeropuerto.taxisAeropuerto.security;

import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        // 🔹 Agregar prefijo ROLE_ para que Spring Security reconozca los roles
        String roleName = "ROLE_" + (user.getRol() != null ? user.getRol().getNombre() : "USER");

        return new org.springframework.security.core.userdetails.User(
                user.getCorreo(), // usuario = correo
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(roleName))
        );
    }
}
