package io.blss.lab1.repository;

import io.blss.lab1.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
    Optional<PromoCode> findPromoCodeByTitle(String title);
}