package io.blss.lab1.service;

import io.blss.lab1.dto.ProductCategoryResponse;
import io.blss.lab1.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public Page<ProductCategoryResponse> getProductCategories(Pageable pageable) {
        final var ProductCategory = productCategoryRepository.findAll(pageable);
        return ProductCategory.map(ProductCategoryResponse::fromProductCategory);
    }
}
