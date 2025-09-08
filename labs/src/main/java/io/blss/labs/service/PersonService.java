package io.blss.labs.service;

import io.blss.labs.dto.OrderRequest;
import io.blss.labs.dto.PaymentInfoResponse;
import io.blss.labs.dto.PersonalInfoResponse;
import io.blss.labs.entity.PaymentInfo;
import io.blss.labs.entity.PersonalInfo;
import io.blss.labs.entity.User;
import io.blss.labs.repository.PaymentInfoRepository;
import io.blss.labs.repository.PersonalInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PaymentInfoRepository paymentInfoRepository;

    private final UserService userService;

    private final PersonalInfoRepository personalInfoRepository;

    public PaymentInfoResponse getActualPaymentInfo() {
        var user =  userService.getCurrentUser();
        return getActualPaymentInfo(user);
    }

    public PaymentInfoResponse getActualPaymentInfo(User user) {
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
        return getPersonalInfo(user);
    }

    public PersonalInfoResponse getPersonalInfo(User user) {
        PersonalInfo personalInfo = user.getPersonalInfo();
        return PersonalInfoResponse.builder()
                .address(personalInfo.getAddress())
                .number(personalInfo.getNumber())
                .build();
    }

    public Page<PaymentInfoResponse> getPaymentInfoList(Pageable pageable) {
        var user = userService.getCurrentUser();
        return getPaymentInfoList(pageable, user);
    }

    public Page<PaymentInfoResponse> getPaymentInfoList(Pageable pageable, User user) {
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
