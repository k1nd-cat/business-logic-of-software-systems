package io.blss.lab1.controller;

import io.blss.lab1.dto.PaymentInfoResponse;
import io.blss.lab1.dto.PersonalInfoResponse;
import io.blss.lab1.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
@Tag(name = "Аккаунт", description = "Управление аккаунтом")
public class PersonController {

    private final PersonService personService;

    @GetMapping("/payment-info/actual")
    @Operation(summary = "Актуальная платежная информация пользователя")
    public PaymentInfoResponse getActualPaymentInfo(){
        return personService.getActualPaymentInfo();
    }

    @GetMapping("/personal-info")
    @Operation(summary = "Получение информации о пользователе")
    public PersonalInfoResponse getPersonalInfo(){
        return personService.getPersonalInfo();
    }


    @GetMapping("/payment-info")
    @Operation(summary = "Список всех Payment info пользователя")
    public Page<PaymentInfoResponse> getPaymentInfoPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return personService.getPaymentInfoList(PageRequest.of(page, size));
    }
}
