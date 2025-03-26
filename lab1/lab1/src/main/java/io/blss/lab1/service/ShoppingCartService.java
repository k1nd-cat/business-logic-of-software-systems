package io.blss.lab1.service;

import io.blss.lab1.dto.ProductInCartResponse;
import io.blss.lab1.entity.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final UserService userService;

    public List<ProductInCartResponse> getProductsInCart() {
        return getUserShoppingCart().getItems().stream().map(ProductInCartResponse::fromCartItem).toList();
    }


    private ShoppingCart getUserShoppingCart() {
        final var user = userService.getCurrentUser();
        return user.getShoppingCart();
    }
}
