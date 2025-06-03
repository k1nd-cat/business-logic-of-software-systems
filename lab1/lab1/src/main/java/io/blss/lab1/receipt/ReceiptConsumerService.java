//package io.blss.lab1.receipt;
//
//
//import com.itextpdf.io.font.PdfEncodings;
//import com.itextpdf.kernel.font.PdfFont;
//import com.itextpdf.kernel.font.PdfFontFactory;
//import io.blss.lab1.entity.Order;
//import io.blss.lab1.repository.OrderRepository;
//import io.blss.lab1.service.OrderService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//@Service
//public class ReceiptConsumerService {
//    private static final Logger log = LoggerFactory.getLogger(ReceiptConsumerService.class);
//    private final OrderRepository orderRepository;
//    private static final String RECEIPTS_DIR = "receipts_pdf";
//    private static final String FONT_PATH = "src/main/resources/fonts/arial.ttf";
//
//    private PdfFont regularFont;
//
//    @Autowired
//    public ReceiptConsumerService(OrderRepository orderRepository) {
//        this.orderRepository = orderRepository;
//        try {
//            Files.createDirectories(Paths.get(RECEIPTS_DIR));
//            File fontFile = new File(FONT_PATH);
//            if (fontFile.exists()) {
//                this.regularFont = PdfFontFactory.createFont(FONT_PATH, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
//            } else {
//                log.warn("Файл шрифта {} не найден. Будет использован шрифт по умолчанию.", FONT_PATH);
//                this.regularFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);
//            }
//
//        } catch (IOException e) {
//            log.error("Не удалось создать директорию для чеков: {} или загрузить шрифт", RECEIPTS_DIR, e);
//        }
//    }
////@RabbitListener(queues = RabbitMQConfig.RECEIPT_QUEUE_NAME)
//    @Transactional // Если обновляете статус заказа
//    public void handleReceiptGenerationTask(Long orderId) {
//        log.info("Получена задача на генерацию PDF чека для заказа ID: {}", orderId);
//
//        Order order = orderRepository.findByIdWithUserAndItemsAndPaymentInfo(orderId).orElse(null);
//
//        if (order == null) {
//            log.error("Заказ с ID: {} не найден. Невозможно сгенерировать PDF чек.", orderId);
//            return;
//        }
//
//        try {
//}
