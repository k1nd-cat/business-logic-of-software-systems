package io.blss.lab1.service;

import io.blss.lab1.dto.CartItemResponse;
import io.blss.lab1.entity.ShoppingCart;
import io.blss.lab1.exception.*;
import io.blss.lab1.repository.CartItemRepository;
import io.blss.lab1.repository.ProductRepository;
import io.blss.lab1.repository.PromoCodeRepository;
import io.blss.lab1.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final UserService userService;
    private final PromoCodeService promoCodeService;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    private final PromoCodeRepository promoCodeRepository;

    private final ShoppingCartRepository shoppingCartRepository;

    public List<CartItemResponse> getProductsInCart() {
        return getUserShoppingCart().getItems().stream().map(CartItemResponse::fromCartItem).toList();
    }

    @Transactional
    public void removeProduct(Long cartItemId) {
        final var cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new CartItemNotFoundException("Заданного товара в корзине не существует")
        );

        final var quantity = cartItem.getQuantity();
        final var product = productRepository.findById(cartItem.getProduct().getId()).orElseThrow(
                () -> new ProductNotFoundException("Заданного продукта не существует")
        );
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void addProductCount(Long cartItemId, Integer quantity) {
        final var cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new CartItemNotFoundException("Заданного продукта нет в корзине")
        );

        final var product = cartItem.getProduct();

        if (quantity > product.getQuantity()) {
            throw new ProductQuantityException("Товара в таком количестве нет в наличии");
        }

        product.setQuantity(product.getQuantity() - quantity);
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        productRepository.save(product);
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeProductQuantity(Long cartItemId, Integer quantity) {
        final var cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new CartItemNotFoundException("Заданного продукта нет в корзине")
        );

        final var product = cartItem.getProduct();

        if (quantity > cartItem.getQuantity()) {
            throw new CartItemQuantityException("В корзине не лежит так много данного товара");
        }

        if (quantity.equals(cartItem.getQuantity())) {
            removeProduct(cartItemId);
            return;
        }

        product.setQuantity(product.getQuantity() + quantity);
        cartItem.setQuantity(cartItem.getQuantity() - quantity);
        productRepository.save(product);
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void addPromoCode(String codeTitle) {
        final var promoCode = promoCodeRepository.findPromoCodeByTitle(codeTitle).orElseThrow(
                () -> new PromoCodeNotFoundException("Заданного промокода не существует")
        );
        final var shoppingCart = getUserShoppingCart();

        if (!promoCodeService.checkForValidityCart(promoCode, shoppingCart)) {
            throw new InvalidPromoCodeException("На данный заказ не может действовать промокод");
        }

        shoppingCart.setPromoCode(promoCode);
        shoppingCartRepository.save(shoppingCart);
    }

    public void removePromoCode() {
        final var shoppingCart = getUserShoppingCart();
        shoppingCart.setPromoCode(null);
    }

    public Double getPrice() {
        final var shoppingCart = getUserShoppingCart();
        var price = shoppingCart.getItems().stream()
                .mapToDouble(product -> product.getProduct().getPrice() * product.getQuantity())
                .sum();

        final var promoCode = shoppingCart.getPromoCode();
        if (promoCodeService.checkForValidityCart(promoCode, shoppingCart))
            price = price / 100 * promoCode.getPercentage();

        return price;
    }

    private ShoppingCart getUserShoppingCart() {
        final var user = userService.getCurrentUser();
        return user.getShoppingCart();
    }
}
