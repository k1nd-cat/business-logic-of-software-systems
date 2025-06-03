package io.blss.lab1.repository;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.User;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT COUNT(o) FROM Order o WHERE o.user = :user")
    long countOrdersByUser(@Param("user") User user);

    Page<Order> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<Order> findAllByStatusOrderByCreatedAtAsc(Order.OrderStatus status, Pageable pageable);

    Page<Order> findAllByCourierAndStatus(User courier, Order.OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.user = :user ORDER BY o.createdAt DESC")
    Page<Order> findAllByUserOrderByCreatedAtDescWithItems(@Param("user") User user, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);

    List<Order> findByStatusAndCreatedAtBefore(Order.OrderStatus status, Instant timeLimit);

//    Order order findByIdAndOrderItemsAndPaymentInfo(@Param("id") Long id, List<AbstractReadWriteAccess.Item> items);
}
