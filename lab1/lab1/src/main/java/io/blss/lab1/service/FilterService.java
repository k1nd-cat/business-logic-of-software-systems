package io.blss.lab1.service;

import io.blss.lab1.dto.CategoryCharacteristicsResponse;
import io.blss.lab1.exception.ProductCategoryNotFoundException;
import io.blss.lab1.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilterService {

    private final ProductCategoryRepository productCategoryRepository;

    public CategoryCharacteristicsResponse getFiltersByCategoryId(Long categoryId) {
        final var category = productCategoryRepository.findById(categoryId).orElseThrow(
                () -> new ProductCategoryNotFoundException("Заданная категория не найдена")
        );

        return CategoryCharacteristicsResponse.fromProductCategory(category);
    }
}
