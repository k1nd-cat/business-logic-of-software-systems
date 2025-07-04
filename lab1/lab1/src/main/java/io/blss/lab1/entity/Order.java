package io.blss.lab1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
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
    private Instant createdAt;

    @Column(name = "delivered_at", columnDefinition = "TIMESTAMP")
    private Date deliveredAt;

    @Column(name = "canceled_at", columnDefinition = "TIMESTAMP")
    private Date canceledAt;

    @Column(name = "delivery_time", columnDefinition = "TIMESTAMP")
    private Date deliveryTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "promo_code_id")
    private PromoCode promoCode;

    @ManyToOne
    @JoinColumn(name = "courier_id")
    private User courier;

    @ManyToOne
    @JoinColumn(name = "payment_info_id")
    private PaymentInfo paymentInfo;

    public enum DeliveryType {
        COURIER,
        PICKUP
    }

    public enum OrderStatus {
        // ожидает оплаты
        PENDING_PAYMENT,
        //        обрабатывается
        PROCESSING,

        //        отправлен
        SHIPPED,

        //        доставлен
        DELIVERED,

        //        отменен
        CANCELLED,
        // не дождался оплаты
        CANCELLED_TIMEOUT
    }
}
