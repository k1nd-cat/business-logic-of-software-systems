package io.blss.lab1.repository;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT COUNT(o) FROM Order o WHERE o.user = :user")
    long countOrdersByUser(@Param("user") User user);

    Page<Order> findAllByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<Order> findAllByStatusOrderByCreatedAtAsc(Order.OrderStatus status, Pageable pageable);

    Page<Order> findAllByCourierAndStatus(User courier, Order.OrderStatus status, Pageable pageable);
}
