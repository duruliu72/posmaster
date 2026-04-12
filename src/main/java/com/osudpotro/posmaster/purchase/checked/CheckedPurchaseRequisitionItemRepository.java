package com.osudpotro.posmaster.purchase.checked;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CheckedPurchaseRequisitionItemRepository extends JpaSpecificationExecutor<CheckedPurchaseRequisitionItem>, JpaRepository<CheckedPurchaseRequisitionItem, Long> {
   Optional<CheckedPurchaseRequisitionItem> findByCheckedPurchaseRequisitionIdAndPurchaseRequisitionItemId(Long checkedPurchaseRequisitionId, Long purchaseRequisitionItemId);
   @Query("""
                SELECT cprItem FROM CheckedPurchaseRequisitionItem cprItem
                WHERE cprItem.checkedPurchaseRequisition.id = :checkedPurchaseRequisitionId
                  AND (:productName IS NULL OR cprItem.product.productName LIKE %:productName%)
            """)
   Page<CheckedPurchaseRequisitionItem> findAllWithFilterItems(@Param("checkedPurchaseRequisitionId") Long checkedPurchaseRequisitionId, @Param("productName") String productName, Pageable pageable);
}
