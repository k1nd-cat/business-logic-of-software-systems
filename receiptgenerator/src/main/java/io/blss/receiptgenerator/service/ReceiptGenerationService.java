package io.blss.receiptgenerator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import io.blss.receiptgenerator.dto.ReceiptDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

@Service
@RequiredArgsConstructor
public class ReceiptGenerationService {

    private final ObjectMapper objectMapper;

    private final MinioService minioService;

    @JmsListener(destination = "generate_receipt_queue")
    public void generateReceipt(String message) {
        try {
            ReceiptDto receiptDto = objectMapper.readValue(message, ReceiptDto.class);
            String filename = receiptDto.getUserId() + "_" + receiptDto.getOrderId() + ".pdf";
            final var pdfFile = generateReceiptPdf(receiptDto, filename);
            minioService.uploadFile(filename, pdfFile);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Received message: " + message + " on receipt-service (port: " + System.getProperty("server.port") + ")");
    }

    private File generateReceiptPdf(ReceiptDto receipt, String filename) throws Exception {
        File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + filename);
        FileOutputStream fos = new FileOutputStream(tempFile);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Add header
        document.add(new Paragraph("Receipt")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20));

        // Create products table
        float[] columnWidths = {3, 1, 2, 2};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // Table headers
        table.addHeaderCell(new Cell().add(new Paragraph("Product")));
        table.addHeaderCell(new Cell().add(new Paragraph("Quantity")));
        table.addHeaderCell(new Cell().add(new Paragraph("Price")));
        table.addHeaderCell(new Cell().add(new Paragraph("Total")));

        // Add products
        DecimalFormat df = new DecimalFormat("#.##");
        double subtotal = 0.0;
        for (ReceiptDto.ReceiptProduct product : receipt.getReceiptProductList()) {
            double total = product.getQuantity() * product.getPrice();
            subtotal += total;
            table.addCell(new Cell().add(new Paragraph(product.getName())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(product.getQuantity()))));
            table.addCell(new Cell().add(new Paragraph("₽" + df.format(product.getPrice()))));
            table.addCell(new Cell().add(new Paragraph("₽" + df.format(total))));
        }

        document.add(table);

        // Add subtotal and discount
        if (receipt.getDiscount() != null && receipt.getDiscount() > 0) {
            double discountPercent = receipt.getDiscount() * 100;
            double discountAmount = subtotal * receipt.getDiscount();
            double finalTotal = subtotal - discountAmount;

            document.add(new Paragraph("Discount: " + df.format(discountPercent) + "%")
                    .setTextAlignment(TextAlignment.RIGHT));
        }

        document.add(new Paragraph("Subtotal: ₽" + df.format(subtotal))
                .setTextAlignment(TextAlignment.RIGHT));

        // Close document
        document.close();

        return tempFile;
    }
}
