package io.blss.lab1.controller;

import io.blss.lab1.dto.CartItemResponse;
import io.blss.lab1.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@Tag(name = "Корзина", description = "Управление корзиной покупок")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping("/items")
    @Operation(summary = "Содержимое корзины")
    public Page<CartItemResponse> getCartItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return shoppingCartService.getProductsInCart(PageRequest.of(page, size));
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Удалить товар")
    public void removeCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.removeProduct(cartItemId);
    }

    @PatchMapping("/items/{cartItemId}/increase")
    @Operation(summary = "Увеличить количество")
    public void increaseQuantity(@PathVariable Long cartItemId,
                                 @RequestParam Integer quantity) {
        shoppingCartService.addProductCount(cartItemId, quantity);
    }

    @PatchMapping("/items/{cartItemId}/decrease")
    @Operation(summary = "Уменьшить количество")
    public void decreaseQuantity(@PathVariable Long cartItemId,
                                 @RequestParam Integer quantity) {
        shoppingCartService.removeProductQuantity(cartItemId, quantity);
    }

    @PostMapping("/promo-codes")
    @Operation(summary = "Применить промокод")
    public void applyPromoCode(@RequestParam String promoCode) {
        shoppingCartService.addPromoCode(promoCode);
    }

    @DeleteMapping("/promo-codes")
    @Operation(summary = "Удалить промокод")
    public void removePromoCode() {
        shoppingCartService.removePromoCode();
    }

    @GetMapping("/total")
    @Operation(summary = "Итоговая стоимость")
    public Double getCartTotal() {
        return shoppingCartService.getPrice();
    }
}
