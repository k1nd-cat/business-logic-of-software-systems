package io.blss.lab1.service;

import io.blss.lab1.dto.ProductInCartResponse;
import io.blss.lab1.entity.ShoppingCart;
import io.blss.lab1.exception.CartItemNotFoundException;
import io.blss.lab1.exception.CartItemQuantityException;
import io.blss.lab1.exception.ProductNotFoundException;
import io.blss.lab1.exception.ProductQuantityException;
import io.blss.lab1.repository.CartItemRepository;
import io.blss.lab1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final UserService userService;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public List<ProductInCartResponse> getProductsInCart() {
        return getUserShoppingCart().getItems().stream().map(ProductInCartResponse::fromCartItem).toList();
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

        product.setQuantity(product.getQuantity() + quantity);
        cartItem.setQuantity(cartItem.getQuantity() - quantity);
        productRepository.save(product);
        cartItemRepository.save(cartItem);
    }

    private ShoppingCart getUserShoppingCart() {
        final var user = userService.getCurrentUser();
        return user.getShoppingCart();
    }
}
