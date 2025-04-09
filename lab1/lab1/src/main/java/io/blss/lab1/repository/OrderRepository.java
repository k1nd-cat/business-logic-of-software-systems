package io.blss.lab1.repository;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT COUNT(o) FROM Order o WHERE o.user = :user")
    long countOrdersByUser(@Param("user") User user);

    List<Order> findAllByUserOrderByCreatedAtDesc(User user);

    List<Order> findAllByStatusOrderByCreatedAtAsc(Order.OrderStatus status);

    List<Order> findAllByCourierAndStatus(User courier, Order.OrderStatus status);
}
