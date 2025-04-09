package io.blss.lab1.controller;

import io.blss.lab1.dto.CategoryCharacteristicsResponse;
import io.blss.lab1.service.FilterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/filters")
@Tag(name = "Фильтры", description = "Работа с характеристиками товаров")
public class CharacteristicController {
    private final FilterService filterService;

    @GetMapping("/categories/{categoryId}/characteristics")
    @Operation(summary = "Получить характеристики категории")
    public CategoryCharacteristicsResponse getCategoryCharacteristics(@PathVariable Long categoryId) {
        return filterService.getFiltersByCategoryId(categoryId);
    }
}