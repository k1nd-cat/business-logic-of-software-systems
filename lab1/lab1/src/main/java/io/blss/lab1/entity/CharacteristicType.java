package io.blss.lab1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "characteristic_type")
public class CharacteristicType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Characteristic> characteristics;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "product_category_characteristic_type",
            joinColumns = @JoinColumn(name = "characteristic_type_id"),
            inverseJoinColumns = @JoinColumn(name = "product_category_id")
    )
    private List<ProductCategory> productCategories;

    public enum Type {
        TEXT,
        NUMERIC
    }
}
