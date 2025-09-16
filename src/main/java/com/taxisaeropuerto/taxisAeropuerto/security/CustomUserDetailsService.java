package com.taxisaeropuerto.taxisAeropuerto.security;

import com.taxisaeropuerto.taxisAeropuerto.entity.User;
import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + usernameOrEmail));

        // Configura autoridad de rol, aunque sea null
        GrantedAuthority authority = new SimpleGrantedAuthority(
                user.getRol() != null ? user.getRol().getNombre() : "USER"
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername() != null ? user.getUsername() : user.getEmail(),
                user.getPassword() != null ? user.getPassword() : "",
                Collections.singletonList(authority)
        );
    }
}
