package com.taxisaeropuerto.taxisAeropuerto.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordTestController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Verificar si una contraseña coincide con un hash guardado en la BD
    @GetMapping("/test-password")
    public String testPassword() {
        String rawPassword = "tobon*1"; // la contraseña en texto plano que intentas usar
        String hashFromDb = "$2a$10$stOqL25AD1e55MjtoGvxm.J5I2DYZEZ3PS0PHWkh8mQ.CIJFkyxhO"; // hash de la BD

        boolean matches = passwordEncoder.matches(rawPassword, hashFromDb);
        return matches ? "✅ Coinciden" : "❌ No coinciden";
    }

    // ✅ Generar un nuevo hash para una contraseña
    @GetMapping("/generate-hash")
    public String generateHash() {
        String rawPassword = "tobon*1"; // cámbiala por la contraseña que quieras guardar
        String encoded = passwordEncoder.encode(rawPassword);
        return "Hash generado: " + encoded;
    }
}
