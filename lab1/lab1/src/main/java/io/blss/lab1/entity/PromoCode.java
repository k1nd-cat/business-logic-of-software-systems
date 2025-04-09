package io.blss.lab1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promo_code")
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(name = "start_date", columnDefinition = "TIMESTAMP")
    private Date startDate;

    @Column(name = "end_date", columnDefinition = "TIMESTAMP")
    private Date endDate;

    private Double percentage;

    @Enumerated(value = EnumType.STRING)
    private Condition condition;

    public enum Condition {
        FIRST_ORDER,
        ALWAYS
    }
}
