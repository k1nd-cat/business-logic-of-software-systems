package io.blss.lab1.repository;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
