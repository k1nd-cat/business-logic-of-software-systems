package io.blss.lab1.service;

import io.blss.lab1.dto.OrderRequest;
import io.blss.lab1.dto.OrderResponse;
import io.blss.lab1.dto.PaymentInfoResponse;
import io.blss.lab1.dto.PersonalInfoResponse;
import io.blss.lab1.entity.PaymentInfo;
import io.blss.lab1.entity.PersonalInfo;
import io.blss.lab1.entity.User;
import io.blss.lab1.repository.PaymentInfoRepository;
import io.blss.lab1.repository.PersonalInfoRepository;
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

    private final PersonalInfoRepository personalInfoRepository;

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

    public PaymentInfo changeActualPaymentInfo(User user, String cardNumber) {
        final var actualPaymentInfo = paymentInfoRepository.findByUserAndIsActual(user, true).orElse(null);

        if(actualPaymentInfo != null) {
            actualPaymentInfo.setActual(false);
            paymentInfoRepository.save(actualPaymentInfo);
        }

        var paymentInfo = paymentInfoRepository.findByUserAndCardNumber(user, cardNumber)
                .orElse(PaymentInfo.builder()
                        .cardNumber(cardNumber)
                        .user(user)
                        .build());
        paymentInfo.setActual(true);
        return paymentInfoRepository.save(paymentInfo);
    }

    public PersonalInfo updatePersonalInfo(User user, OrderRequest orderRequest) {
        var personalInfo = personalInfoRepository.findByUser(user)
                .orElse(PersonalInfo.builder().user(user).build());
        personalInfo = orderRequest.updatePersonalInfo(personalInfo);
        return personalInfoRepository.save(personalInfo);
    }
}
