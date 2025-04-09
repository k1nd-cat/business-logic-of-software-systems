package io.blss.lab1.dto;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.PersonalInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String number;
    private String cardNumber;
    private String address;
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
