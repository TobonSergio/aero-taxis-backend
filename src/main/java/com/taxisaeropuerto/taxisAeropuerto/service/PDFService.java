package com.taxisaeropuerto.taxisAeropuerto.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.taxisaeropuerto.taxisAeropuerto.entity.Reserva;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import com.lowagie.text.Image; // ✅ Este es el correcto
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PDFService {

    @Value("${custom.pdf-path}")
    private String rutaPDF;

    public String generarComprobante(Reserva reserva) throws DocumentException, IOException {
        File carpeta = new File(rutaPDF);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        String nombreArchivo = "comprobante_" + reserva.getId_reserva() + ".pdf";
        String rutaCompleta = rutaPDF + nombreArchivo;

        Document document = new Document(PageSize.A4, 50, 50, 70, 50);
        PdfWriter.getInstance(document, new FileOutputStream(rutaCompleta));
        document.open();

        // 🖼️ Logo (centrado arriba)
        try {
            String logoPath = "src/main/resources/static/logo-taxis-rojos-aero-2.jpeg"; // ✅ Ruta del logo
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(120, 120); // Ajusta el tamaño del logo
            logo.setAlignment(Element.ALIGN_CENTER); // Centrar el logo
            document.add(logo);

            document.add(new Paragraph("\n")); // Espacio debajo del logo
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cargar el logo: " + e.getMessage());
        }

        // 🟡 Encabezado
        Font tituloFont = new Font(Font.HELVETICA, 20, Font.BOLD, new Color(0, 102, 204));
        Paragraph titulo = new Paragraph("Comprobante de Reserva", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        // 🔹 Línea separadora
        document.add(new Paragraph(" "));
        document.add(new LineSeparator());

        // 🟢 Información del cliente
        Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

        document.add(new Paragraph("Datos del Cliente:", headerFont));
        document.add(new Paragraph("Nombre: " + reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellido(), normalFont));
        document.add(new Paragraph("Correo: " + reserva.getCliente().getUsuario().getCorreo(), normalFont));
        document.add(new Paragraph(" ")); // Espacio

        // 🟠 Detalles de la reserva en formato de tabla
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        table.addCell(celda("ID de Reserva:", true));
        table.addCell(celda(String.valueOf(reserva.getId_reserva()), false));

        table.addCell(celda("Destino:", true));
        table.addCell(celda(reserva.getDestino(), false));

        table.addCell(celda("Lugar de Recogida:", true));
        table.addCell(celda(reserva.getLugarRecogida(), false));

        table.addCell(celda("Fecha:", true));
        table.addCell(celda(reserva.getFechaReserva().toString(), false));

        table.addCell(celda("Hora:", true));
        table.addCell(celda(reserva.getHoraReserva().toString(), false));

        table.addCell(celda("Comentarios:", true));
        table.addCell(celda(reserva.getComentarios() != null ? reserva.getComentarios() : "-", false));

        document.add(table);

        // 🔹 Línea final
        document.add(new LineSeparator());
        document.add(new Paragraph(" "));

        // 🕒 Pie de página
        Font footerFont = new Font(Font.HELVETICA, 10, Font.ITALIC, new Color(120, 120, 120));
        Paragraph footer = new Paragraph("Generado el " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), footerFont);
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);

        document.close();

        System.out.println("✅ PDF generado: " + rutaCompleta);
        return nombreArchivo;
    }

    // 🧩 Método auxiliar para crear celdas con o sin negrita
    private PdfPCell celda(String texto, boolean bold) {
        Font font = new Font(Font.HELVETICA, 12, bold ? Font.BOLD : Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        return cell;
    }
}
