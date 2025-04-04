package io.blss.lab1.controller;

import io.blss.lab1.dto.CategoryCharacteristicsResponse;
import io.blss.lab1.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class CharacteristicController {

    private final FilterService filterService;

    @GetMapping("/get-by-category/{categoryId}")
    public CategoryCharacteristicsResponse getCharacteristicsByCategory(@PathVariable Long categoryId) {
        return filterService.getFiltersByCategoryId(categoryId);
    }
}
