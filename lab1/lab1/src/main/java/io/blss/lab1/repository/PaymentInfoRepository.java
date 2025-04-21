package io.blss.lab1.repository;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.PaymentInfo;
import io.blss.lab1.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {
        List<PaymentInfo> findByUser(User user);

        Optional<PaymentInfo> findByIdAndUser(Long id, User user);

        Optional<PaymentInfo> findByUserAndCardNumber(User user, String cardNumber);

        Optional<PaymentInfo> findByUserAndIsActual(User user, Boolean isActual);

        Page<PaymentInfo> findAllByUser(User user, Pageable pageable);
}