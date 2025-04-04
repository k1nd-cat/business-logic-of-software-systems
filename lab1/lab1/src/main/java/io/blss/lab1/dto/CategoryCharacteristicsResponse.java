package io.blss.lab1.dto;

import io.blss.lab1.entity.Characteristic;
import io.blss.lab1.entity.CharacteristicType;
import io.blss.lab1.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCharacteristicsResponse {
    private Long id;
    private String title;
    private String description;
    private List<CharacteristicTypeResponse> characteristicTypes;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CharacteristicTypeResponse {
        private Long id;
        private String title;
        private String description;
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CharacteristicResponse {
        private Long id;
        private String textValue;
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
