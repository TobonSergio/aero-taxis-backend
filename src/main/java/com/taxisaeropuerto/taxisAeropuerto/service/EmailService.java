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
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("sestebantmontoya@gmail.com"); // remitente validado en SendGrid
        message.setTo(toEmail);
        message.setSubject("Verificación de cuenta para Taxis Aeropuerto");

        // 🔥 URL dinámica según tu properties
        message.setText("Hola!\n\nPara verificar tu cuenta, por favor, haz clic en el siguiente enlace:\n"
                + frontendUrl + "/verify?token=" + token);

        mailSender.send(message);
    }
}
