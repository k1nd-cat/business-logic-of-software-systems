package io.blss.lab1.repository;

import io.blss.lab1.entity.Product;
import io.blss.lab1.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByProductCategory(ProductCategory category);

    List<Product> findAllByTitleStartingWith(String prefix);
}
