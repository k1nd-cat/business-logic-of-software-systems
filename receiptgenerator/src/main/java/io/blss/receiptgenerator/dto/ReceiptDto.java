package io.blss.receiptgenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDto {
    private Long userId;
    private Long orderId;
    private Double price;
    private Double discount;
    private List<ReceiptProduct> receiptProductList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiptProduct {
        private String name;
        private Integer quantity;
        private Double price;
    }
}
