package com.osudpotro.posmaster.purchase.purchasecart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PurchaseCartRepository extends JpaSpecificationExecutor<PurchaseCart>, JpaRepository<PurchaseCart, Long> {
    @Query("SELECT pc FROM PurchaseCart pc WHERE pc.createdBy.id = :userId")
    Optional<PurchaseCart> findPurchaseCartByUserId(@Param("userId") Long userId);
    @Query("SELECT pc FROM PurchaseCart pc WHERE pc.id = :id")
    Optional<PurchaseCart> findPurchaseCartById(@Param("id") Long id);
}
