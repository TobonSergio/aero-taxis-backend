package com.taxisaeropuerto.taxisAeropuerto.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.taxisaeropuerto.taxisAeropuerto.entity.Asignacion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import com.lowagie.text.Image;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PDFService {

    private final QRGenerator qrService;

    public byte[] generarComprobante(Asignacion asignacion) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 50, 50, 70, 50);
            PdfWriter.getInstance(document, baos);
            document.open();

            // 🖼️ Logo
            try {
                String logoPath = "src/main/resources/static/logo-taxis-rojos-aero-2.jpeg";
                Image logo = Image.getInstance(logoPath);
                logo.scaleToFit(120, 120);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
                document.add(new Paragraph("\n"));
            } catch (Exception ignored) {}

            // 🟡 Título
            Font tituloFont = new Font(Font.HELVETICA, 20, Font.BOLD, new Color(0, 102, 204));
            Paragraph titulo = new Paragraph("Comprobante de Asignación de Reserva", tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            document.add(new LineSeparator());
            document.add(new Paragraph(" "));

            Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 12, Font.NORMAL);

            // 🔵 Cliente
            document.add(new Paragraph("Datos del Cliente:", headerFont));
            document.add(new Paragraph(
                    "Nombre: " + asignacion.getReserva().getCliente().getNombre() +
                            " " + asignacion.getReserva().getCliente().getApellido(),
                    normalFont
            ));
            document.add(new Paragraph(
                    "Correo: " + asignacion.getReserva().getCliente().getUsuario().getCorreo(),
                    normalFont
            ));
            document.add(new Paragraph(" "));

            // 🟠 Tabla de Reserva
            PdfPTable tablaReserva = new PdfPTable(2);
            tablaReserva.setWidthPercentage(100);

            tablaReserva.addCell(celda("ID Reserva:", true));
            tablaReserva.addCell(celda(asignacion.getReserva().getIdReserva().toString(), false));

            tablaReserva.addCell(celda("Destino:", true));
            tablaReserva.addCell(celda(asignacion.getReserva().getDestino(), false));

            tablaReserva.addCell(celda("Lugar de Recogida:", true));
            tablaReserva.addCell(celda(asignacion.getReserva().getLugarRecogida(), false));

            tablaReserva.addCell(celda("Fecha:", true));
            tablaReserva.addCell(celda(asignacion.getReserva().getFechaReserva().toString(), false));

            tablaReserva.addCell(celda("Hora:", true));
            tablaReserva.addCell(celda(asignacion.getReserva().getHoraReserva().toString(), false));

            tablaReserva.addCell(celda("Comentarios:", true));
            tablaReserva.addCell(celda(
                    asignacion.getReserva().getComentarios() != null ?
                            asignacion.getReserva().getComentarios() : "-", false
            ));

            document.add(tablaReserva);
            document.add(new Paragraph(" "));

            // 🚗 Unidad
            if (asignacion.getUnidad() != null) {
                document.add(new Paragraph("Datos de la Unidad:", headerFont));

                PdfPTable tablaUnidad = new PdfPTable(2);
                tablaUnidad.setWidthPercentage(100);

                tablaUnidad.addCell(celda("Placa:", true));
                tablaUnidad.addCell(celda(asignacion.getUnidad().getPlaca(), false));

                tablaUnidad.addCell(celda("Modelo:", true));
                tablaUnidad.addCell(celda(asignacion.getUnidad().getSerie(), false));

                document.add(tablaUnidad);
                document.add(new Paragraph(" "));
            }

            // Chofer
            if (asignacion.getChofer() != null) {
                document.add(new Paragraph("Datos del Chofer:", headerFont));

                PdfPTable tablaChofer = new PdfPTable(2);
                tablaChofer.setWidthPercentage(100);

                tablaChofer.addCell(celda("Nombre:", true));
                tablaChofer.addCell(celda(asignacion.getChofer().getNombre() + " " +
                        asignacion.getChofer().getApellido(), false));

                tablaChofer.addCell(celda("Teléfono:", true));
                tablaChofer.addCell(celda(asignacion.getChofer().getTelefono(), false));

                tablaChofer.addCell(celda("Licencia:", true));
                tablaChofer.addCell(celda(asignacion.getChofer().getLicenciaConduccion(), false));

                document.add(tablaChofer);
                document.add(new Paragraph(" "));
            }

            // 📅 Detalles
            document.add(new Paragraph("Detalles de la Asignación:", headerFont));
            document.add(new Paragraph(
                    "Fecha de Asignación: " +
                            asignacion.getFechaAsignacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    normalFont
            ));
            document.add(new Paragraph("Estado: " + asignacion.getEstado().name(), normalFont));

            document.add(new LineSeparator());
            document.add(new Paragraph(" "));

            // 🟩 CONTENIDO DEL QR (bien formateado)
            String contenidoQR =
                    "ID Reserva: " + asignacion.getReserva().getIdReserva() + "\n" +
                            "Cliente: " + asignacion.getReserva().getCliente().getNombre() + " " +
                            asignacion.getReserva().getCliente().getApellido() + "\n" +
                            "Destino: " + asignacion.getReserva().getDestino() + "\n" +
                            "Fecha: " + asignacion.getReserva().getFechaReserva() + "\n" +
                            "Hora: " + asignacion.getReserva().getHoraReserva();

            byte[] qrBytes = qrService.generarQR(contenidoQR);

            Image qrImg = Image.getInstance(qrBytes);
            qrImg.scaleToFit(180, 180);
            qrImg.setAlignment(Element.ALIGN_CENTER);
            qrImg.setSpacingBefore(20);

            document.add(qrImg);

            // Footer
            Font footerFont = new Font(Font.HELVETICA, 10, Font.ITALIC, new Color(120, 120, 120));
            Paragraph footer = new Paragraph(
                    "Generado el " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    footerFont
            );
            footer.setAlignment(Element.ALIGN_RIGHT);
            footer.setSpacingBefore(20);
            document.add(footer);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }

    private PdfPCell celda(String texto, boolean bold) {
        Font font = new Font(Font.HELVETICA, 12, bold ? Font.BOLD : Font.NORMAL);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5);
        return cell;
    }
}
