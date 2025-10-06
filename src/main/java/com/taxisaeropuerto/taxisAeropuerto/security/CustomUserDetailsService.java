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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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


        if (user.getPassword() == null) {
             user.setPassword("");
        }

        String username = user.getEmail();
        String password = user.getPassword();

        org.springframework.security.core.userdetails.User userSpringSecurity = new org.springframework.security.core.userdetails.User(username, password, Collections.singleton(authority));

        return userSpringSecurity;

    }
}
