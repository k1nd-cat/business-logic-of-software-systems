package io.blss.lab1.controller;

import io.blss.lab1.dto.ProductResponse;
import io.blss.lab1.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "Товары", description = "Управление товарами")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Список товаров")
    public Page<ProductResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getAllProducts(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Информация о товаре")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск товаров")
    public Page<ProductResponse> searchProducts(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getProductsByPrefix(query, PageRequest.of(page, size));
    }

    @GetMapping("/categories/{categoryId}")
    @Operation(summary = "Товары категории")
    public Page<ProductResponse> getCategoryProducts(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getProductsByCategoryId(categoryId, PageRequest.of(page, size));
    }

    @PostMapping("/{productId}/cart")
    @Operation(summary = "Добавить в корзину")
    public void addToCart(@PathVariable Long productId,
                          @RequestParam(defaultValue = "1") Integer quantity) {
        productService.addProduct2Cart(productId, quantity);
    }

    @GetMapping("/categories/{categoryId}/filter")
    @Operation(summary = "Фильтрация товаров")
    public List<ProductResponse> getFilteredProducts(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "") String query,
            @RequestParam("filter") List<String> filters) {
        Map<Long, Long> filtersMap = filters.stream()
                .map(filter -> filter.split(":"))
                .filter(parts -> parts.length == 2)
                .collect(Collectors.toMap(
                        parts -> Long.parseLong(parts[0]),
                        parts -> Long.parseLong(parts[1])
                ));
        return productService.getProductsWithFilters(categoryId, query, filtersMap);
    }
}
