package io.blss.lab1.repository;

import io.blss.lab1.entity.CartItem;
import io.blss.lab1.entity.Product;
import io.blss.lab1.entity.ShoppingCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(ShoppingCart cart, Product product);

    Page<CartItem> findAllByCart(ShoppingCart shoppingCart, Pageable pageable);

    long countByCart(ShoppingCart cart);
}
