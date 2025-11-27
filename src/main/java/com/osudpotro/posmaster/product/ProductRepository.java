package com.osudpotro.posmaster.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaSpecificationExecutor<Product>,JpaRepository<Product,Long> {
    boolean existsByProductName(String name);

    @Transactional
    @Modifying
    @Query("delete from ProductCategory pc WHERE pc.product.id = :productId")
    void deleteCategoryByProduct(Long productId);
}
