package com.osudpotro.posmaster.purchase.requisition;

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

public interface PurchaseRequisitionItemRepository extends JpaSpecificationExecutor<PurchaseRequisitionItem>, JpaRepository<PurchaseRequisitionItem, Long> {
    @Query("""
                SELECT pri FROM PurchaseRequisitionItem pri
                WHERE pri.purchaseRequisition.id = :purchaseRequisitionId
                  AND pri.product.id = :productId
                  AND pri.productDetail.id = :productDetailId
            """)
    Optional<PurchaseRequisitionItem> findPurchaseRequisitionItem(
            @Param("purchaseRequisitionId") Long purchaseRequisitionId,
            @Param("productId") Long productId,
            @Param("productDetailId") Long productDetailId
    );

    @Query("""
                SELECT pri FROM PurchaseRequisitionItem pri
                WHERE pri.purchaseRequisition.id = :purchaseRequisitionId
                  AND (:productName IS NULL OR pri.product.productName LIKE %:productName%)
            """)
    Page<PurchaseRequisitionItem> findPurchaseRequisitionItems(@Param("purchaseRequisitionId") Long purchaseRequisitionId, @Param("productName") String productName, Pageable pageable);

    @Transactional
    @Modifying
    @Query("""
                DELETE FROM PurchaseRequisitionItem pri
                WHERE pri.id in :purchaseRequisitionItemIds
                  AND pri.purchaseRequisition.id = :purchaseRequisitionId
            """)
    int removeBulkPurchaseRequisitionItem(@Param("purchaseRequisitionId") Long purchaseRequisitionId, @Param("purchaseRequisitionItemIds") List<Long> purchaseRequisitionItemIds);
    @Transactional
    @Modifying
    @Query("""
                update PurchaseRequisitionItem pri
                set pri.addableStatus = :addableStatus
                WHERE pri.id in :purchaseRequisitionItemIds
                  AND pri.purchaseRequisition.id = :purchaseRequisitionId
            """)
//    @Query("update Organization o set o.status = :status where o.id in :ids")
    int updateBulkForAddableItem(@Param("purchaseRequisitionId") Long purchaseRequisitionId, @Param("purchaseRequisitionItemIds") List<Long> purchaseRequisitionItemIds,@Param("addableStatus") Integer addableStatus);
}
