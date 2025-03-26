package io.blss.lab1.service;

import io.blss.lab1.dto.ProductCategoryResponse;
import io.blss.lab1.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductCategoryResponse> getProductCategories() {
        final var ProductCategory = productCategoryRepository.findAll();
        return ProductCategory.stream().map(ProductCategoryResponse::fromProductCategory).toList();
    }
}
