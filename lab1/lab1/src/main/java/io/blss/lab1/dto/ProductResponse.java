package io.blss.lab1.dto;

import io.blss.lab1.entity.Characteristic;
import io.blss.lab1.entity.CharacteristicType;
import io.blss.lab1.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    Long id;
    String title;
    String description;
    Double price;
    Integer quantity;
    private List<ProductCharacteristicResponse> characteristics;

    @Data
    @Builder
    public static class ProductCharacteristicResponse {
        private Long characteristicId;
        private String characteristicValue;
        private Long typeId;
        private String typeTitle;
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
