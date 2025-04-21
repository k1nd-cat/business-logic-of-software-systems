package io.blss.lab1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PersonalInfoResponse {
    @Schema(description = "Номер телефона", example = "+79312345473")
    private String number;

    @Schema(description = "Адрес", example = "ул. Марата д.12, 2 подъезд, 2 этаж, кв.45")
    private String address;

}
