package com.taxisaeropuerto.taxisAeropuerto.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // ✅ ahora usamos tu propiedad existente en application.properties
    @Value("${custom.frontemailredirecturl}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom("sestebantmontoya@gmail.com"); // ⚠️ remitente debe estar validado en SendGrid
            message.setTo(toEmail);
            message.setSubject("Verificación de cuenta para Taxis Aeropuerto");

            message.setText("Hola!\n\nPara verificar tu cuenta, haz clic aquí:\n"
                    + frontendUrl + "/verify?token=" + token);

            mailSender.send(message);

            // ✅ Log de éxito
            System.out.println("✅ Correo de verificación enviado a: " + toEmail);

        } catch (Exception e) {
            // ❌ Log de error
            System.err.println("❌ Error enviando correo a " + toEmail + ": " + e.getMessage());
            e.printStackTrace(); // imprime el stack completo
        }
    }
}
