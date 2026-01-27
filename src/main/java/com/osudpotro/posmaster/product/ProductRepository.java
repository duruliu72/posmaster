package com.osudpotro.posmaster.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

public interface ProductRepository extends JpaSpecificationExecutor<Product>, JpaRepository<Product, Long> {
    boolean existsByProductName(String name);
//    Example
    @Query(value = """
                SELECT  p.productCode as productCode,p.id as id
                FROM Product p
                JOIN FETCH p.productGenerics pg
                WHERE p.id = :id
            """,nativeQuery = true)
    Optional<Map<String,Object>> findByIdWithProductGenericStatus2(@Param("id") Long id, @Param("status") Integer status);
    @Query("""
                SELECT DISTINCT p
                FROM Product p
                JOIN FETCH p.productGenerics pg
                WHERE p.id = :id
            """)
    Optional<Product> findByIdWithProductGenericStatus(@Param("id") Long id, @Param("status") Integer status);

    @Transactional
    @Modifying
    @Query("delete from ProductCategory pc WHERE pc.product.id = :productId")
    void deleteProductCategoryByProduct(Long productId);

    Product findTopByOrderByCreatedAtDesc();
}
