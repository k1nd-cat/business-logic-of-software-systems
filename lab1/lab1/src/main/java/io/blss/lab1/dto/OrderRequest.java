package io.blss.lab1.dto;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.PersonalInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Запрос на создание заказа")
@Data
@Builder
public class OrderRequest {
    @Schema(description = "Номер телефона", example = "+79123456789")
    @NotBlank(message = "Номер телефона обязателен")
    private String number;

    @Schema(description = "Номер карты", example = "4111111111111111")
    @NotBlank(message = "Номер карты обязателен")
    private String cardNumber;

    @Schema(description = "Адрес доставки", example = "ул. Ленина, д. 10, кв. 5")
    @NotBlank(message = "Адрес доставки обязателен")
    @Size(max = 255, message = "Адрес слишком длинный")
    private String address;

    @Schema(description = "Тип доставки", example = "COURIER")
    @NotNull(message = "Тип доставки обязателен")
    private Order.DeliveryType deliveryType;

    @Schema(description = "Id платежной информации", example = "238")
    @Min(1)
    private Long paymentInfoId;


    public Order toOrder() {
        return Order.builder()
                .address(address)
                .deliveryType(deliveryType)
                .build();
    }

    public PersonalInfo updatePersonalInfo(PersonalInfo personalInfo) {
        personalInfo.setNumber(number);
        personalInfo.setAddress(address);
        return personalInfo;
    }
}
