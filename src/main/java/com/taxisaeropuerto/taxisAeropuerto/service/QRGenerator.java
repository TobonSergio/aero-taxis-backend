package com.taxisaeropuerto.taxisAeropuerto.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.taxisaeropuerto.taxisAeropuerto.entity.Reserva;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Service
public class QRGenerator {

    // 🔹 Inyecta la ruta desde application.properties
    @Value("${custom.qr-path}")
    private String rutaQR;

    public String generarQR(Reserva reserva) throws WriterException, IOException {
        File carpeta = new File(rutaQR);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        String nombreArchivo = "QR_" + reserva.getId_reserva() + ".png";
        String rutaCompleta = rutaQR + nombreArchivo;

        String contenidoQR = "Reserva ID: " + reserva.getId_reserva() +
                "\nCliente: " + reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellido() +
                "\nDestino: " + reserva.getDestino() +
                "\nFecha y hora: " + reserva.getFechaReserva() + " " + reserva.getHoraReserva();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(contenidoQR, BarcodeFormat.QR_CODE, 200, 200);

        Path path = FileSystems.getDefault().getPath(rutaCompleta);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        System.out.println("QR generado: " + rutaCompleta);
        return nombreArchivo;
    }
}
