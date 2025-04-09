package io.blss.lab1.repository;

import io.blss.lab1.entity.Product;
import io.blss.lab1.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByProductCategory(ProductCategory category, Pageable pageable);

    Page<Product> findAllByTitleStartingWithIgnoreCase(String prefix, Pageable pageable);

    List<Product> findAllByProductCategoryAndTitleStartsWithIgnoreCase(ProductCategory category, String prefix);

    Page<Product> findAllByProductCategoryIdAndTitleStartsWithIgnoreCase(
            Long categoryId,
            String prefix,
            Pageable pageable
    );

    @Query("""
        SELECT p FROM Product p
        JOIN p.characteristics c
        WHERE p.productCategory.id = :categoryId
        AND LOWER(p.title) LIKE LOWER(CONCAT(:prefix, '%'))
        AND (:filtersMap IS NULL OR 
            (SELECT COUNT(DISTINCT ct.id) FROM CharacteristicType ct 
             WHERE ct.id IN :filterTypeIds) = 
            (SELECT COUNT(DISTINCT c2.type.id) FROM p.characteristics c2
             WHERE c2.type.id IN :filterTypeIds AND c2.id IN :filterCharacteristicIds))
        GROUP BY p
        HAVING COUNT(DISTINCT c.type.id) = :filterCount
        """)
    Page<Product> findAllByCategoryAndTitleAndCharacteristics(
            @Param("categoryId") Long categoryId,
            @Param("prefix") String prefix,
            @Param("filterTypeIds") Set<Long> filterTypeIds,
            @Param("filterCharacteristicIds") Set<Long> filterCharacteristicIds,
            @Param("filterCount") long filterCount,
            Pageable pageable
    );

    default Page<Product> findAllByCategoryAndTitleAndCharacteristics(
            Long categoryId,
            String prefix,
            Map<Long, Long> filtersMap,
            Pageable pageable) {

        if (filtersMap == null || filtersMap.isEmpty()) {
            return findAllByProductCategoryIdAndTitleStartsWithIgnoreCase(
                    categoryId, prefix, pageable);
        }

        Set<Long> typeIds = filtersMap.keySet();
        Set<Long> characteristicIds = new HashSet<>(filtersMap.values());

        return findAllByCategoryAndTitleAndCharacteristics(
                categoryId,
                prefix,
                typeIds,
                characteristicIds,
                filtersMap.size(),
                pageable);
    }
}
