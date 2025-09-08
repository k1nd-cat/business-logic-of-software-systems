package io.blss.labs.repository;

import io.blss.labs.entity.PaymentInfo;
import io.blss.labs.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {
        List<PaymentInfo> findByUser(User user);

        Optional<PaymentInfo> findByIdAndUser(Long id, User user);

        Optional<PaymentInfo> findByUserAndCardNumber(User user, String cardNumber);

        Optional<PaymentInfo> findByUserAndIsActual(User user, Boolean isActual);

        Page<PaymentInfo> findAllByUser(User user, Pageable pageable);
}