package com.taxisaeropuerto.taxisAeropuerto.service;

import com.taxisaeropuerto.taxisAeropuerto.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Intenta encontrar el usuario por el nombre de usuario O por el correo electrónico
        return userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByCorreo(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con: " + usernameOrEmail));
    }
}