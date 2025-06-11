package io.blss.lab1.robokassa;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payment {

    private String invoiceRef;

    private String comment;

    private String commentData;

    private double amount;
}