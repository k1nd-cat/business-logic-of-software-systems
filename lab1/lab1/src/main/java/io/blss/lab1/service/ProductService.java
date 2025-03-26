package io.blss.lab1.service;

import io.blss.lab1.dto.ProductResponse;
import io.blss.lab1.entity.CartItem;
import io.blss.lab1.entity.ProductCategory;
import io.blss.lab1.entity.User;
import io.blss.lab1.exception.ProductCategoryNotFoundException;
import io.blss.lab1.exception.ProductNotFoundException;
import io.blss.lab1.exception.ProductQuantityException;
import io.blss.lab1.repository.CartItemRepository;
import io.blss.lab1.repository.ProductCategoryRepository;
import io.blss.lab1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductResponse> getAllProducts() {
        final var products = productRepository.findAll();
        return products.stream().map(ProductResponse::fromProduct).toList();
    }

    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        final var category = productCategoryRepository.findById(categoryId).orElseThrow(
                () -> new ProductCategoryNotFoundException("Заданной категории не существует")
        );
        final var products = productRepository.findAllByProductCategory(category);
        return products.stream().map(ProductResponse::fromProduct).toList();
    }

    public List<ProductResponse> getProductsByPrefix(String prefix) {
        final var products = productRepository.findAllByTitleStartingWith(prefix);
        return products.stream().map(ProductResponse::fromProduct).toList();
    }

    @Transactional
    public void addProduct2Cart(Long productId, Integer quantity) {
        if (quantity <= 0)
            throw new ProductQuantityException("Нельзя добавить в корзину отрицательное количество товара");

        final var user = userService.getCurrentUser();
        if (user == null) throw new InsufficientAuthenticationException("Пользователь не авторизован");
        final var product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Невозможно добавить продукт в корзину, поскольку его не существует")
        );

        if (user.getRole() != User.Role.ROLE_USER)
            throw new AccessDeniedException("Сотрудники не могут делать заказы");

        if (product.getQuantity() == 0)
            throw new ProductQuantityException("Товара нет в наличии");

        if (product.getQuantity() < quantity)
            throw new ProductQuantityException("Нет продукта в таком количестве");

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        var cartItem = cartItemRepository.findByCartAndProduct(user.getShoppingCart(), product)
                .orElse(CartItem.builder().product(product).cart(user.getShoppingCart()).quantity(0).build());
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepository.save(cartItem);
    }
}
