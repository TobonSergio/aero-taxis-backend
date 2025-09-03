package com.taxisaeropuerto.taxisAeropuerto.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();

        // Define el remitente (debe ser el correo que verificaste en SendGrid)
        message.setFrom("sestebantmontoya@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Verificación de cuenta para Taxis Aeropuerto");
        message.setText("Hola!\n\nPara verificar tu cuenta, por favor, haz clic en el siguiente enlace:\n"
                + "http://localhost:8080/api/auth/verify?token=" + token);

        mailSender.send(message);
    }
}