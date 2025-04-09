package io.blss.lab1.controller;

import io.blss.lab1.dto.CartItemResponse;
import io.blss.lab1.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping("/product/get-all")
    public Page<CartItemResponse> getProductsInCart(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return shoppingCartService.getProductsInCart(pageable);
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
        shoppingCartService.removeProductQuantity(cartItemId, quantity);
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
}
