package io.blss.lab1.controller;

import io.blss.lab1.dto.ProductInCartResponse;
import io.blss.lab1.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping("/products/get-all")
    public List<ProductInCartResponse> getProductsInCart() {
        return shoppingCartService.getProductsInCart();
    }
}
