package io.blss.lab1.dto;

import io.blss.lab1.entity.Characteristic;
import io.blss.lab1.entity.CharacteristicType;
import io.blss.lab1.entity.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Schema(description = "Характеристики категории товаров")
@Data
@Builder
public class CategoryCharacteristicsResponse {
    @Schema(description = "ID категории", example = "1")
    private Long id;

    @Schema(description = "Название категории", example = "Электроника")
    private String title;

    @Schema(description = "Описание категории", example = "Техника и электронные устройства")
    private String description;

    @Schema(description = "Список типов характеристик")
    private List<CharacteristicTypeResponse> characteristicTypes;

    @Schema(description = "Тип характеристики")
    @Data
    @Builder
    public static class CharacteristicTypeResponse {
        @Schema(description = "ID типа характеристики", example = "1")
        private Long id;

        @Schema(description = "Название типа", example = "Цвет")
        private String title;

        @Schema(description = "Описание типа", example = "Основной цвет устройства")
        private String description;

        @Schema(description = "Список характеристик")
        private List<CharacteristicResponse> characteristics;

        public static CharacteristicTypeResponse fromCharacteristicType(CharacteristicType type) {
            final var characteristicList = type.getCharacteristics()
                    .stream().map(CharacteristicResponse::fromCharacteristic).toList();
            return CharacteristicTypeResponse.builder()
                    .id(type.getId())
                    .title(type.getTitle())
                    .description(type.getDescription())
                    .characteristics(characteristicList)
                    .build();
        }
    }

    @Schema(description = "Характеристика товара")
    @Data
    @Builder
    public static class CharacteristicResponse {
        @Schema(description = "ID характеристики", example = "1")
        private Long id;

        @Schema(description = "Текстовое значение", example = "Черный")
        private String textValue;

        @Schema(description = "Числовое значение", example = "15.5")
        private Double numericValue;

        public static CharacteristicResponse fromCharacteristic(Characteristic characteristic) {
            return CharacteristicResponse.builder()
                    .id(characteristic.getId())
                    .textValue(characteristic.getTextValue())
                    .numericValue(characteristic.getNumericValue())
                    .build();
        }
    }

    public static CategoryCharacteristicsResponse fromProductCategory(ProductCategory category) {
        final var characteristicTypeList = category.getCharacteristicTypes()
                .stream().map(CharacteristicTypeResponse::fromCharacteristicType).toList();
        return CategoryCharacteristicsResponse.builder()
                .id(category.getId())
                .title(category.getTitle())
                .description(category.getDescription())
                .characteristicTypes(characteristicTypeList)
                .build();
    }
}
