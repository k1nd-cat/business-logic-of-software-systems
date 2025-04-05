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

    @DeleteMapping("/product/{cartItemId}")
    public void removeProduct(@PathVariable Long cartItemId) {
        shoppingCartService.removeProduct(cartItemId);
    }

    @PostMapping("/add/product/{cartItemId}/count/{quantity}")
    public void addProductCount(@PathVariable Long cartItemId, @PathVariable Integer quantity) {
        shoppingCartService.addProductCount(cartItemId, quantity);
    }

    @DeleteMapping("/product/{cartItemId}/count/{quantity}")
    public void removeProductCount(@PathVariable Long cartItemId, @PathVariable Integer quantity) {
        shoppingCartService.addProductCount(cartItemId, quantity);
    }

    @PostMapping("promo-code/{promoCode}/add")
    public void addPromoCode(@PathVariable String promoCode) {
        shoppingCartService.addPromoCode(promoCode);
    }

    @DeleteMapping("promo-code")
    public void removePromoCode() {
        shoppingCartService.removePromoCode();
    }

    @GetMapping("price")
    public Double getPrice() {
        return shoppingCartService.getPrice();
    }
//    TODO: перейти к оплате (возможно, уже есть в сервисе корзины)
}
