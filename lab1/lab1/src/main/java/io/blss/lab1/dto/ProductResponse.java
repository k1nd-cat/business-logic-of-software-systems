package io.blss.lab1.dto;

import io.blss.lab1.entity.Characteristic;
import io.blss.lab1.entity.CharacteristicType;
import io.blss.lab1.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Schema(description = "Полная информация о товаре")
@Data
@Builder
public class ProductResponse {
    @Schema(description = "ID товара", example = "10")
    private Long id;

    @Schema(description = "Название товара",
            example = "Смартфон Galaxy S23 Ultra",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Подробное описание товара",
            example = "Флагманский смартфон с S-пером и камерой 200 МП")
    private String description;

    @Schema(description = "Цена товара",
            example = "1299.99",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Double price;

    @Schema(description = "Доступное количество на складе",
            example = "15",
            minimum = "0")
    private Integer quantity;

    @Schema(description = "Список характеристик товара")
    private List<ProductCharacteristicResponse> characteristics;

    @Schema(description = "Характеристика товара")
    @Data
    @Builder
    public static class ProductCharacteristicResponse {
        @Schema(description = "ID характеристики", example = "25")
        private Long characteristicId;

        @Schema(description = "Значение характеристики",
                example = "Черный",
                requiredMode = Schema.RequiredMode.REQUIRED)
        private String characteristicValue;

        @Schema(description = "ID типа характеристики", example = "3")
        private Long typeId;

        @Schema(description = "Название типа характеристики",
                example = "Цвет",
                requiredMode = Schema.RequiredMode.REQUIRED)
        private String typeTitle;

        @Schema(description = "Описание типа характеристики",
                example = "Основной цвет корпуса")
        private String typeDescription;

        public static ProductCharacteristicResponse fromCharacteristic(Characteristic characteristic) {
            final var characteristicValue = characteristic.getType().getType() == CharacteristicType.Type.TEXT
                    ? characteristic.getTextValue()
                    : characteristic.getNumericValue().toString();
            return ProductCharacteristicResponse.builder()
                    .characteristicId(characteristic.getId())
                    .characteristicValue(characteristicValue)
                    .typeId(characteristic.getType().getId())
                    .typeTitle(characteristic.getType().getTitle())
                    .typeDescription(characteristic.getType().getDescription())
                    .build();
        }
    }

    public static ProductResponse fromProduct(Product product) {
        final var characteristics = product.getCharacteristics()
                .stream()
                .map(ProductCharacteristicResponse::fromCharacteristic)
                .toList();

        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .characteristics(characteristics)
                .build();
    }
}
