package io.blss.lab1.controller;

import io.blss.lab1.dto.ProductResponse;
import io.blss.lab1.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get-all")
    public List<ProductResponse> getProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/get/id/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/get")
    public List<ProductResponse> getProductsByPrefix(@RequestParam(defaultValue = "") String prefix) {
        return productService.getProductsByPrefix(prefix);
    }

    @GetMapping("/get/{categoryId}")
    public List<ProductResponse> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategoryId(categoryId);
    }

    @PostMapping("add-to-cart/{productId}")
    public void addToCart(@PathVariable Long productId, @RequestParam(defaultValue = "1") Integer quantity) {
        productService.addProduct2Cart(productId, quantity);
    }

    @GetMapping("/get/category-id/{categoryId}/filters")
    public List<ProductResponse> getProductsWithFilters(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "") String prefix,
            @RequestParam("filter") List<String> filters
    ) {
        Map<Long, Long> filtersMap = filters.stream()
                .map(filter -> filter.split(":"))
                .filter(parts -> parts.length == 2) // Защита от некорректных данных
                .collect(Collectors.toMap(
                        parts -> Long.parseLong(parts[0]), // Ключ (ID фильтра)
                        parts -> Long.parseLong(parts[1])  // Значение
                ));
        return productService.getProductsWithFilters(categoryId, prefix, filtersMap);
    }
}
