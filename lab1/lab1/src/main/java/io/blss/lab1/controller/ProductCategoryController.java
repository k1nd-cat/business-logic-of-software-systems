package io.blss.lab1.controller;

import io.blss.lab1.dto.ProductCategoryResponse;
import io.blss.lab1.service.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Tag(name = "Категории", description = "Работа с категориями товаров")
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    @GetMapping
    @Operation(summary = "Список категорий")
    public Page<ProductCategoryResponse> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productCategoryService.getProductCategories(PageRequest.of(page, size));
    }
}
