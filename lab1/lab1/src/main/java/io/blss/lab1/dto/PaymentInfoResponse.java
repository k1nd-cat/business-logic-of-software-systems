package io.blss.lab1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInfoResponse {
    @Schema(description = "Id", example = "865")
    Long id;
    @Schema(description = "Номер карты", example = "5423 4356 7866 9889")
    String cardNumber;
    @Schema(description = "Актуальный", example = "true")
    boolean isActual;
}