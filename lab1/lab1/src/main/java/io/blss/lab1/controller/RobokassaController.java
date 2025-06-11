package io.blss.lab1.controller;

import io.blss.lab1.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/robokassa")
@Tag(name = "Платеж робокассы")
public class RobokassaController {

    private final OrderService orderService;

    @GetMapping("result")
    public void resultRobokassa(
            @RequestParam("OutSum") String outSum,
            @RequestParam("InvId") Long invId,
            @RequestParam("SignatureValue") String signatureValue,
            @RequestParam(value = "IsTest", defaultValue = "0") int isTest,
            @RequestParam(value = "Culture", required = false) String culture) {
        orderService.successfulPayment(invId);
    }
}
