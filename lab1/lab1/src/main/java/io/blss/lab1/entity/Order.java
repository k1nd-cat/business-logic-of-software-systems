package io.blss.lab1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(nullable = false)
    //сумма заказа
    private Double orderAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PROCESSING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryType deliveryType;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "delivered_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deliveredAt;

    @Column(name = "delivery_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime deliveryTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "promo_code_id")
    private PromoCode promoCode;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private User courier;

    public enum DeliveryType {
        COURIER,
        PICKUP
    }

    public enum OrderStatus {
//        обрабатывается
        PROCESSING,

//        отправлен
        SHIPPED,

//        доставлен
        DELIVERED,

//        отменен
        CANCELLED,
    }
}
