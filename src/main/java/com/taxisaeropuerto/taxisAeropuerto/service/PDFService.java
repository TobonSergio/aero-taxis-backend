package com.taxisaeropuerto.taxisAeropuerto.service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.taxisaeropuerto.taxisAeropuerto.entity.Reserva;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PDFService {

    // 🔹 Inyecta la ruta desde application.properties
    @Value("${custom.pdf-path}")
    private String rutaPDF;

    public String generarComprobante(Reserva reserva) throws DocumentException, IOException {
        File carpeta = new File(rutaPDF);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        String nombreArchivo = "comprobante_" + reserva.getId_reserva() + ".pdf";
        String rutaCompleta = rutaPDF + nombreArchivo;

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(rutaCompleta));
        document.open();

        document.add(new Paragraph("Comprobante de Reserva"));
        document.add(new Paragraph("ID Reserva: " + reserva.getId_reserva()));
        document.add(new Paragraph("Cliente: " + reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellido()));
        document.add(new Paragraph("Destino: " + reserva.getDestino()));
        document.add(new Paragraph("Fecha y hora: " + reserva.getFechaReserva() + " " + reserva.getHoraReserva()));
        document.add(new Paragraph("Comentarios: " + reserva.getComentarios()));

        document.close();

        System.out.println("PDF generado: " + rutaCompleta);
        return nombreArchivo;
    }
}
