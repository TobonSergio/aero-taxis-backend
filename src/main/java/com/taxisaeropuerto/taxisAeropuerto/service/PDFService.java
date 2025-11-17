package com.taxisaeropuerto.taxisAeropuerto.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.taxisaeropuerto.taxisAeropuerto.entity.Asignacion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import com.lowagie.text.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PDFService {

    @Value("${custom.pdf-path}")
    private String rutaPDF;

    public String generarComprobante(Asignacion asignacion) throws DocumentException, IOException {
// ✅ Crear carpeta si no existe
        File carpeta = new File(rutaPDF);
        if (!carpeta.exists()) {
            boolean creada = carpeta.mkdirs();
            if (!creada) {
                throw new IOException("❌ No se pudo crear la carpeta para guardar PDFs: " + rutaPDF);
            }
        }

// ✅ Asegurar que termine con "/"
        if (!rutaPDF.endsWith(File.separator)) {
            rutaPDF += File.separator;
        }

        String nombreArchivo = "comprobante_asignacion_" + asignacion.getIdAsignacion() + ".pdf";
        String rutaCompleta = rutaPDF + nombreArchivo;

        Document document = new Document(PageSize.A4, 50, 50, 70, 50);
        PdfWriter.getInstance(document, new FileOutputStream(rutaCompleta));
        document.open();

        // 🖼️ Logo
        try {
            String logoPath = "src/main/resources/static/logo-taxis-rojos-aero-2.jpeg";
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(120, 120);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
            document.add(new Paragraph("\n"));
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo cargar el logo: " + e.getMessage());
        }

        // 🟡 Encabezado
        Font tituloFont = new Font(Font.HELVETICA, 20, Font.BOLD, new Color(0, 102, 204));
        Paragraph titulo = new Paragraph("Comprobante de Asignación de Reserva", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        document.add(titulo);

        document.add(new LineSeparator());
        document.add(new Paragraph(" "));

        // 🔵 Sección Cliente
        Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

        document.add(new Paragraph("Datos del Cliente:", headerFont));
        document.add(new Paragraph("Nombre: " + asignacion.getReserva().getCliente().getNombre() + " " + asignacion.getReserva().getCliente().getApellido(), normalFont));
        document.add(new Paragraph("Correo: " + asignacion.getReserva().getCliente().getUsuario().getCorreo(), normalFont));
        document.add(new Paragraph(" "));

        // 🟠 Sección Reserva
        document.add(new Paragraph("Detalles de la Reserva:", headerFont));
        PdfPTable tablaReserva = new PdfPTable(2);
        tablaReserva.setWidthPercentage(100);
        tablaReserva.setSpacingBefore(10);

        tablaReserva.addCell(celda("ID Reserva:", true));
        tablaReserva.addCell(celda(String.valueOf(asignacion.getReserva().getIdReserva()), false));

        tablaReserva.addCell(celda("Destino:", true));
        tablaReserva.addCell(celda(asignacion.getReserva().getDestino(), false));

        tablaReserva.addCell(celda("Lugar de Recogida:", true));
        tablaReserva.addCell(celda(asignacion.getReserva().getLugarRecogida(), false));

        tablaReserva.addCell(celda("Fecha:", true));
        tablaReserva.addCell(celda(asignacion.getReserva().getFechaReserva().toString(), false));

        tablaReserva.addCell(celda("Hora:", true));
        tablaReserva.addCell(celda(asignacion.getReserva().getHoraReserva().toString(), false));

        tablaReserva.addCell(celda("Comentarios:", true));
        tablaReserva.addCell(celda(asignacion.getReserva().getComentarios() != null ? asignacion.getReserva().getComentarios() : "-", false));

        document.add(tablaReserva);
        document.add(new Paragraph(" "));

        // 🚗 Sección Unidad
        if (asignacion.getUnidad() != null) {
            document.add(new Paragraph("Datos de la Unidad:", headerFont));
            PdfPTable tablaUnidad = new PdfPTable(2);
            tablaUnidad.setWidthPercentage(100);
            tablaUnidad.setSpacingBefore(10);

            tablaUnidad.addCell(celda("Placa:", true));
            tablaUnidad.addCell(celda(asignacion.getUnidad().getPlaca(), false));

            tablaUnidad.addCell(celda("Modelo:", true));
            tablaUnidad.addCell(celda(asignacion.getUnidad().getSerie(), false));

            document.add(tablaUnidad);
            document.add(new Paragraph(" "));
        }

        // 👨‍✈️ Sección Chofer
        if (asignacion.getChofer() != null) {
            document.add(new Paragraph("Datos del Chofer:", headerFont));
            PdfPTable tablaChofer = new PdfPTable(2);
            tablaChofer.setWidthPercentage(100);
            tablaChofer.setSpacingBefore(10);

            tablaChofer.addCell(celda("Nombre:", true));
            tablaChofer.addCell(celda(asignacion.getChofer().getNombre() + " " + asignacion.getChofer().getApellido(), false));

            tablaChofer.addCell(celda("Teléfono:", true));
            tablaChofer.addCell(celda(asignacion.getChofer().getTelefono(), false));

            tablaChofer.addCell(celda("Licencia:", true));
            tablaChofer.addCell(celda(asignacion.getChofer().getLicenciaConduccion(), false));

            document.add(tablaChofer);
            document.add(new Paragraph(" "));
        }

        // 📅 Detalle Asignación
        document.add(new Paragraph("Detalles de la Asignación:", headerFont));
        document.add(new Paragraph("Fecha de Asignación: " + asignacion.getFechaAsignacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), normalFont));
        document.add(new Paragraph("Estado: " + asignacion.getEstado().name(), normalFont));

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

    // 🧩 Utilidad para crear celdas
    private PdfPCell celda(String texto, boolean bold) {
        Font font = new Font(Font.HELVETICA, 12, bold ? Font.BOLD : Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        return cell;
    }
}
