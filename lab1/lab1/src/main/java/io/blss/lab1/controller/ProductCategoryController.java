package io.blss.lab1.controller;

import io.blss.lab1.dto.ProductCategoryResponse;
import io.blss.lab1.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-category")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping("/get-all")
    public List<ProductCategoryResponse> getProductCategories() {
        return productCategoryService.getProductCategories();
    }
}
