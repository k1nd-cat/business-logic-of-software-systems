package io.blss.lab1.service;

import io.blss.lab1.dto.ProductResponse;
import io.blss.lab1.entity.CartItem;
import io.blss.lab1.entity.Characteristic;
import io.blss.lab1.entity.User;
import io.blss.lab1.exception.ProductCategoryNotFoundException;
import io.blss.lab1.exception.ProductNotFoundException;
import io.blss.lab1.exception.ProductQuantityException;
import io.blss.lab1.repository.CartItemRepository;
import io.blss.lab1.repository.ProductCategoryRepository;
import io.blss.lab1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserService userService;
    private final CartItemRepository cartItemRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        final var products = productRepository.findAll(pageable);
        return products.map(ProductResponse::fromProduct);
    }

    public Page<ProductResponse> getProductsByCategoryId(Long categoryId, Pageable pageable) {
        final var category = productCategoryRepository.findById(categoryId).orElseThrow(
                () -> new ProductCategoryNotFoundException("Заданной категории не существует")
        );
        final var products = productRepository.findAllByProductCategory(category, pageable);
        return products.map(ProductResponse::fromProduct);
    }

    public Page<ProductResponse> getProductsByPrefix(String prefix, Pageable pageable) {
        final var products = productRepository.findAllByTitleStartingWithIgnoreCase(prefix, pageable);
        return products.map(ProductResponse::fromProduct);
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

        if (!Objects.equals(user.getRole().getName(), "USER_ROLE"))
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

    public List<ProductResponse> getProductsWithFilters(Long categoryId, String prefix, Map<Long, Long> filtersMap) {
        final var category = productCategoryRepository.findById(categoryId).orElseThrow(
                () -> new ProductCategoryNotFoundException("Заданная категория не найдена")
        );
        final var products = productRepository.findAllByProductCategoryAndTitleStartsWithIgnoreCase(category, prefix);
        final var filteredProducts = products.stream()
                .filter(product -> {
                    Set<Characteristic> productCharacteristics = new HashSet<>(product.getCharacteristics());

                    return filtersMap.entrySet().stream()
                            .allMatch(entry -> {
                                Long requiredTypeId = entry.getKey();
                                Long requiredCharacteristicId = entry.getValue();

                                return productCharacteristics.stream()
                                        .anyMatch(ch ->
                                                ch.getType().getId().equals(requiredTypeId) &&
                                                        ch.getId().equals(requiredCharacteristicId)
                                        );
                            });
                })
                .toList();

        return filteredProducts.stream().map(ProductResponse::fromProduct).toList();
    }

    public Page<ProductResponse> getProductsWithFilters(
            Long categoryId,
            String prefix,
            Map<Long, Long> filtersMap,
            Pageable pageable
    ) {

        if (!productCategoryRepository.existsById(categoryId)) {
            throw new ProductCategoryNotFoundException("Заданная категория не найдена");
        }

        if (filtersMap == null || filtersMap.isEmpty()) {
            return productRepository
                    .findAllByProductCategoryIdAndTitleStartsWithIgnoreCase(
                            categoryId,
                            prefix,
                            pageable)
                    .map(ProductResponse::fromProduct);
        }

        return productRepository
                .findAllByCategoryAndTitleAndCharacteristics(
                        categoryId,
                        prefix,
                        filtersMap,
                        pageable)
                .map(ProductResponse::fromProduct);
    }

    public ProductResponse getProductById(Long id) {
        final var product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("Заданного товара не существует")
        );
        return ProductResponse.fromProduct(product);
    }
}
