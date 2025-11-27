package com.osudpotro.posmaster.product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductGenericRepository extends JpaRepository<ProductGeneric,Long> {
    boolean existsByProductIdAndGenericIdAndGenericUnitId(
            Long productId, Long genericId, Long genericUnitId);
}