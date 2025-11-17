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
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String roleName = "ROLE_" + (user.getRol() != null ? user.getRol().getNombre() : "USER");

        // Usuario Google → password null → PERMITIRLO
        if (user.getPassword() == null) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getCorreo())
                    .password("") // nunca null
                    .authorities(new SimpleGrantedAuthority(roleName))
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(!user.getEnabled())
                    .build();
        }

        // Usuario normal
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getCorreo())
                .password(user.getPassword())
                .authorities(new SimpleGrantedAuthority(roleName))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.getEnabled())
                .build();
    }
}
