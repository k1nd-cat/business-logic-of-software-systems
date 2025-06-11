package io.blss.lab1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RobokassaRequestDto {
    private String invoiceRef;
    private double amount;
    private String comment;
}
