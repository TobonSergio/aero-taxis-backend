package com.taxisaeropuerto.taxisAeropuerto.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${custom.frontemailredirecturl}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Método general para enviar correo de verificación
    public void sendVerificationEmail(String toEmail, String token, boolean isCliente) {
        System.out.println("🔐 Clave SendGrid: " + System.getenv("SENDGRID_API_KEY"));

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("sestebantmontoya@gmail.com");
            message.setTo(toEmail);
            message.setSubject("Verificación de cuenta para Taxis Aeropuerto");

            // 👇 diferencia clave: agregamos el parámetro type
            String verificationUrl = frontendUrl + "/verify?token=" + token + "&type=" + (isCliente ? "cliente" : "usuario");

            message.setText("Hola!\n\nPara verificar tu cuenta, haz clic aquí:\n" + verificationUrl);

            mailSender.send(message);
            System.out.println("✅ Correo de verificación enviado a: " + toEmail);

        } catch (Exception e) {
            System.err.println("❌ Error enviando correo a " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Métodos específicos según el tipo de registro
    public void sendClienteVerificationEmail(String toEmail, String token) {
        sendVerificationEmail(toEmail, token, true);
    }

    public void sendUsuarioVerificationEmail(String toEmail, String token) {
        sendVerificationEmail(toEmail, token, false);
    }
}
