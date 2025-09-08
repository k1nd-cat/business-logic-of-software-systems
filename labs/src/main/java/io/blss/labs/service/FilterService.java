package io.blss.labs.service;

import io.blss.labs.dto.CategoryCharacteristicsResponse;
import io.blss.labs.exception.ProductCategoryNotFoundException;
import io.blss.labs.repository.ProductCategoryRepository;
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
