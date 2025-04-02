package io.blss.lab1.controller;

import io.blss.lab1.dto.ProductInCartResponse;
import io.blss.lab1.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping("/product/get-all")
    public List<ProductInCartResponse> getProductsInCart() {
        return shoppingCartService.getProductsInCart();
    }

    @PostMapping("/product/{cartItemId}/remove")
    public void removeProduct(@PathVariable Long cartItemId) {
        shoppingCartService.removeProduct(cartItemId);
    }

//    TODO: увеличить количество товара
//    TODO: уменьшить количество товара
//    TODO: перейти к оплате (возможно, уже есть в сервисе корзины)
}
