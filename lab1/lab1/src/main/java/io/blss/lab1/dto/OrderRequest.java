package io.blss.lab1.dto;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.PersonalInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Запрос на создание заказа")
@Data
@Builder
public class OrderRequest {
    @Schema(description = "Номер телефона", example = "+79123456789")
    private String number;

    @Schema(description = "Номер карты", example = "4111111111111111")
    private String cardNumber;

    @Schema(description = "Адрес доставки", example = "ул. Ленина, д. 10, кв. 5")
    private String address;

    @Schema(description = "Тип доставки", example = "COURIER")
    private Order.DeliveryType deliveryType;

    public Order toOrder() {
        return Order.builder()
                .address(address)
                .deliveryType(deliveryType)
                .build();
    }

    public PersonalInfo tupdatePersonalInfo(PersonalInfo personalInfo) {
        personalInfo.setNumber(number);
        personalInfo.setCardNumber(cardNumber);
        return personalInfo;
    }
}
