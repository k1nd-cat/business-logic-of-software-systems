package io.blss.lab1.controller;

import io.blss.lab1.dto.ProductResponse;
import io.blss.lab1.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get-all")
    public List<ProductResponse> getProducts() {
        return productService.getAllProducts();
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
}
