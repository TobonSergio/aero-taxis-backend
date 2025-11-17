package com.taxisaeropuerto.taxisAeropuerto.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRGenerator {

    public byte[] generarQR(String contenido) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    contenido,
                    BarcodeFormat.QR_CODE,
                    400,
                    400,
                    hints
            );

            ByteArrayOutputStream pngOutput = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutput);
            return pngOutput.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando QR", e);
        }
    }
}
