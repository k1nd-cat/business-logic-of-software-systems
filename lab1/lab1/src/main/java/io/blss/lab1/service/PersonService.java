package io.blss.lab1.service;

import io.blss.lab1.dto.OrderResponse;
import io.blss.lab1.dto.PaymentInfoResponse;
import io.blss.lab1.dto.PersonalInfoResponse;
import io.blss.lab1.entity.PaymentInfo;
import io.blss.lab1.entity.PersonalInfo;
import io.blss.lab1.entity.User;
import io.blss.lab1.repository.PaymentInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PaymentInfoRepository paymentInfoRepository;

    private final UserService userService;

    public PaymentInfoResponse getActualPaymentInfo() {
        final User user = userService.getCurrentUser();
        final var actualPaymentInfo = paymentInfoRepository.findByUserAndIsActual(user, true).orElse(null);

        return actualPaymentInfo != null ?
                PaymentInfoResponse.builder()
                        .id(actualPaymentInfo.getId())
                .isActual(actualPaymentInfo.isActual())
                        .cardNumber(actualPaymentInfo.getCardNumber())
                .build() : null;
    }


    public PersonalInfoResponse getPersonalInfo() {
        final User user = userService.getCurrentUser();
        PersonalInfo personalInfo = user.getPersonalInfo();
        return PersonalInfoResponse.builder()
                .address(personalInfo.getAddress())
                .number(personalInfo.getNumber())
                .build();
    }

    public Page<PaymentInfoResponse> getPaymentInfoList(Pageable pageable) {

        final User user = userService.getCurrentUser();

        Page<PaymentInfo> paymentInfos = paymentInfoRepository.findAllByUser(user, pageable);

        return paymentInfos.map((paymentInfo) -> PaymentInfoResponse.builder()
                .id(paymentInfo.getId())
                .cardNumber(paymentInfo.getCardNumber())
                .isActual(paymentInfo.isActual())
                .build()
        );
    }
}
