package io.blss.labs.service;

import io.blss.labs.dto.CartItemResponse;
import io.blss.labs.entity.ShoppingCart;
import io.blss.labs.exception.*;
import io.blss.labs.repository.CartItemRepository;
import io.blss.labs.repository.ProductRepository;
import io.blss.labs.repository.PromoCodeRepository;
import io.blss.labs.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final UserService userService;

    private final PromoCodeService promoCodeService;

    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final PromoCodeRepository promoCodeRepository;

    private final ShoppingCartRepository shoppingCartRepository;

    public Page<CartItemResponse> getProductsInCart(Pageable pageable) {
        var shoppingCart = userService.getCurrentUser().getShoppingCart();
        return getProductsInCart(pageable, shoppingCart);
    }
    
    public Page<CartItemResponse> getProductsInCart(Pageable pageable, ShoppingCart shoppingCart) {
        final var cartItems = cartItemRepository.findAllByCart(shoppingCart, pageable);
        return cartItems.map(CartItemResponse::fromCartItem);
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
    public void addProductCount(Long cartItemId, Long quantity) {
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
    public void removeProductQuantity(Long cartItemId, Long quantity) {
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
        var shoppingCart = userService.getCurrentUser().getShoppingCart();
        addPromoCode(codeTitle, shoppingCart);
    }

    @Transactional
    public void addPromoCode(String codeTitle, ShoppingCart shoppingCart) {
        final var promoCode = promoCodeRepository.findPromoCodeByTitle(codeTitle).orElseThrow(
                () -> new PromoCodeNotFoundException("Заданного промокода не существует")
        );

        if (!promoCodeService.checkForValidityCart(promoCode)) {
            throw new InvalidPromoCodeException("На данный заказ не может действовать этот промокод");
        }

        shoppingCart.setPromoCode(promoCode);
        shoppingCartRepository.save(shoppingCart);
    }

    public void removePromoCode() {
        var shoppingCart = userService.getCurrentUser().getShoppingCart();
        removePromoCode(shoppingCart);
    }

    public void removePromoCode(ShoppingCart shoppingCart) {
        shoppingCart.setPromoCode(null);
        shoppingCartRepository.save(shoppingCart);
    }

    public Double getPrice() {
        var shoppingCart = userService.getCurrentUser().getShoppingCart();
        return getPrice(shoppingCart);
    }

    public Double getPrice(ShoppingCart shoppingCart) {
        var price = shoppingCart.getItems().stream()
                .mapToDouble(product -> product.getProduct().getPrice() * product.getQuantity())
                .sum();

        final var promoCode = shoppingCart.getPromoCode();
        if (promoCode != null && promoCodeService.checkForValidityCart(promoCode))
            price = price / 100 * (100 - promoCode.getPercentage());

        return price;
    }
}
