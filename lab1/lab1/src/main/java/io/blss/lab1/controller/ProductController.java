package io.blss.lab1.controller;

import io.blss.lab1.dto.ProductResponse;
import io.blss.lab1.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<ProductResponse> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/get/id/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/get")
    public Page<ProductResponse> getProductsByPrefix(
            @RequestParam(defaultValue = "") String prefix,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getProductsByPrefix(prefix, pageable);
    }

    @GetMapping("/get/{categoryId}")
    public Page<ProductResponse> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.getProductsByCategoryId(categoryId, pageable);
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
                .filter(parts -> parts.length == 2)
                .collect(Collectors.toMap(
                        parts -> Long.parseLong(parts[0]),
                        parts -> Long.parseLong(parts[1])
                ));
        return productService.getProductsWithFilters(categoryId, prefix, filtersMap);
    }
}
