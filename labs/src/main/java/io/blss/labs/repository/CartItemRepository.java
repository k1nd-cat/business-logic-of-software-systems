package io.blss.labs.repository;

import io.blss.labs.entity.CartItem;
import io.blss.labs.entity.Product;
import io.blss.labs.entity.ShoppingCart;
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
