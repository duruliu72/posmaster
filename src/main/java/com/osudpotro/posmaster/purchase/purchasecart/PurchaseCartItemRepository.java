package com.osudpotro.posmaster.purchase.purchasecart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PurchaseCartItemRepository extends JpaSpecificationExecutor<PurchaseCartItem>, JpaRepository<PurchaseCartItem, Long> {
    @Query("""
                SELECT pci FROM PurchaseCartItem pci
                WHERE pci.purchaseCart.id = :purchaseCartId
                  AND pci.product.id = :productId
                  AND pci.productDetail.id = :productDetailId
            """)
    Optional<PurchaseCartItem> findPurchaseCartItem(
            @Param("purchaseCartId") Long purchaseCartId,
            @Param("productId") Long productId,
            @Param("productDetailId") Long productDetailId
    );

    @Query("""
                SELECT pci FROM PurchaseCartItem pci
                WHERE pci.purchaseCart.id = :purchaseCartId
                  AND (:productName IS NULL OR pci.product.productName LIKE %:productName%)
            """)
    Page<PurchaseCartItem> findPurchaseCartItems(@Param("purchaseCartId") Long purchaseCartId, @Param("productName") String productName, Pageable pageable);

    @Transactional
    @Modifying
    @Query("""
                DELETE FROM PurchaseCartItem pci
                WHERE pci.id in :purchaseCartItemIds
                  AND pci.purchaseCart.id = :purchaseCartId
            """)
    int removeBulkPurchaseCartItem(@Param("purchaseCartId") Long purchaseCartId, @Param("purchaseCartItemIds") List<Long> purchaseCartItemIds);
}
