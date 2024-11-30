package com.cnpm.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cnpm.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Long> countAllByProductCode(String productCode);

	@Query(value = """
			    SELECT *
			    FROM Product p
			    WHERE p.productId IN (
			        SELECT MIN(productId)
			        FROM Product
			        WHERE isUsed = 0
			        GROUP BY productCode
			    )
			""", nativeQuery = true, countQuery = "SELECT COUNT(*) FROM (SELECT DISTINCT productCode FROM Product) tmp")
	Page<Product> findDistinctProduct(Pageable pageable);

	@Query(value = """
			    SELECT *
			    FROM Product p
			    WHERE p.productId IN (
			        SELECT MIN(productId)
			        FROM Product
			        WHERE category = :category AND isUsed = 0
			        GROUP BY productCode
			    )
			    ORDER BY productCode ASC
			""", nativeQuery = true, countQuery = """
			    SELECT COUNT(*)
			    FROM (SELECT DISTINCT productCode FROM Product WHERE category = :category) AS tmp
			""")
	Page<Product> findDistinctProductsByProductCodeAndCategory(@Param("category") String category, Pageable pageable);//

	Long countByCategory(String category);

	@Query(value = "SELECT COUNT(*) FROM (SELECT DISTINCT productCode FROM Product) tmp", nativeQuery = true)
	Long countDistinct();

	@Query(value = """
			    SELECT *
			    FROM Product p
			    WHERE p.productId IN (
			        SELECT MIN(productId)
			        FROM Product
			        WHERE (LOWER(productName) LIKE LOWER(CONCAT('%', :keyword, '%')))
			          AND isUsed = 0
			        GROUP BY productCode
			    )
			""", nativeQuery = true)
	List<Product> findDistinctProductsByKeyword(@Param("keyword") String keyword);
	
	Optional<Product> findByProductCode(String productCode);

	//Lay products co chung co chung productCode
    @Query(value = """
    	    SELECT p.*
    	    FROM Product p
    	    WHERE p.productCode = :productCode
    	""", nativeQuery = true)
    List<Product> findProductsByProductCode(@Param("productCode") String productCode);
    
    @Query(value = """
    	    SELECT p.*
    	    FROM Product p
    	    WHERE p.productId IN (
    	        SELECT MIN(productId)
    	        FROM Product
    	        WHERE isUsed = 0
    	        GROUP BY productCode
    	    )
    	""", nativeQuery = true)
    List<Product> findDistinctProductsByProductCode();
	Optional<Long> countAllByProductCodeAndIsUsedFalse(String productCode);

    Optional<Long> countAllByProductCodeAndIsUsedFalseOrIsUsedNull(String productCode);

    Optional<Product> findFirstByProductCode(String productCode);
    Optional<Product> findFirstByProductCodeAndIsUsedFalse(String productCode);

    @Query(value = """
    	    SELECT *
    	    FROM Product p
    	    WHERE p.productId IN (
    	        SELECT MIN(productId)
    	        FROM Product
    	        WHERE isUsed = 0
    	          AND (
    	              LOWER(productName) LIKE LOWER(CONCAT('%', :keyword, '%'))
    	          )
    	        GROUP BY productCode
    	    )
    	""", nativeQuery = true)
    	List<Product> findDistinctProductsBySingleKeyword(@Param("keyword") String keyword);

	List<Product> findDistinctProductsByCategory(String category);
	//Tim dua theo ten san pham
	@Query(value = """
		    SELECT *
		    FROM Product p
		    WHERE p.productId IN (
		        SELECT MIN(productId)
		        FROM Product
		        WHERE isUsed = 0
		        GROUP BY productCode
		    )
		    AND LOWER(p.productName) LIKE LOWER(CONCAT('%', :productName, '%'))
		""", 
		countQuery = """
		    SELECT COUNT(*)
		    FROM Product p
		    WHERE p.productId IN (
		        SELECT MIN(productId)
		        FROM Product
		        WHERE isUsed = 0
		        GROUP BY productCode
		    )
		    AND LOWER(p.productName) LIKE LOWER(CONCAT('%', :productName, '%'))
		""", 
		nativeQuery = true)
		Page<Product> findDistinctProductsByProductNameContaining(
		    @Param("productName") String productName,
		    Pageable pageable
		);
	//Dem san pham duoc loc theo ten
	@Query(value = """
		    SELECT COUNT(DISTINCT p.productCode)
		    FROM Product p
		    WHERE p.productId IN (
		        SELECT MIN(productId)
		        FROM Product
		        WHERE isUsed = 0
		        GROUP BY productCode
		    )
		    AND LOWER(p.productName) LIKE LOWER(CONCAT('%', :productName, '%'))
		""", nativeQuery = true)
		long countDistinctProductsByProductNameContaining(@Param("productName") String productName);
}
