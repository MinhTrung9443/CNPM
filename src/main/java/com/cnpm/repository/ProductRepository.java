package com.cnpm.repository;

import com.cnpm.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Long> countAllByProductCode(String productCode);
    @Query(value = """
    SELECT *
    FROM Product p
    WHERE p.productId IN (
        SELECT MIN(productId)
        FROM Product
        GROUP BY productCode
    )
""", nativeQuery = true,
            countQuery = "SELECT COUNT(*) FROM (SELECT DISTINCT productCode FROM Product) tmp")
    Page<Product> findDistinctProduct(Pageable pageable);

    @Query(value = """
    SELECT *
    FROM Product p
    WHERE p.productId IN (
        SELECT MIN(productId)
        FROM Product
        WHERE category = :category
        GROUP BY productCode
    )
    ORDER BY productCode ASC
""",
            nativeQuery = true,
            countQuery = """
    SELECT COUNT(*) 
    FROM (SELECT DISTINCT productCode FROM Product WHERE category = :category) AS tmp
""")
    Page<Product> findDistinctProductsByProductCodeAndCategory(@Param("category") String category, Pageable pageable);


    @Query(value = """
    SELECT *
    FROM Product p
    WHERE p.productId IN (
        SELECT MIN(productId)
        FROM Product
        WHERE category = :category
        GROUP BY productCode
    )
    ORDER BY productCode ASC
""",
            nativeQuery = true
            )
    List<Product> findDistinctProductsByProductCodeAndCategoryWithoutPaging(@Param("category") String category);
    Long countByCategory(String category);

    @Query(value = "SELECT COUNT(*) FROM (SELECT DISTINCT productCode FROM Product) tmp",nativeQuery = true)
    Long countDistinct();

    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategoryName();
}

