package com.taxisaeropuerto.taxisAeropuerto.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashed = encoder.encode("tobon*1"); // tu contraseña real
        System.out.println(hashed);
    }
}