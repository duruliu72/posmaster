package com.osudpotro.posmaster.purchase.transfer;

import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PurchaseRequisitionItemTransferRepository extends JpaSpecificationExecutor<PurchaseRequisitionItemTransfer>, JpaRepository<PurchaseRequisitionItemTransfer, Long> {
   Optional<PurchaseRequisitionItemTransfer> findByPurchaseRequisitionTransferIdAndPurchaseRequisitionItemId(Long purchaseRequisitionTransferId, Long purchaseRequisitionItemId);
   @Query("""
                SELECT pri FROM PurchaseRequisitionItemTransfer pri
                WHERE pri.purchaseRequisitionTransfer.id = :purchaseRequisitionTransferId
                ORDER BY pri.product.manufacturer.id
            """)
   List<PurchaseRequisitionItemTransfer> findEntityList(@Param("purchaseRequisitionTransferId") Long purchaseRequisitionTransferId);
   @Query("""
                SELECT pri FROM PurchaseRequisitionItemTransfer pri
                WHERE pri.purchaseRequisitionTransfer.id = :purchaseRequisitionTransferId
                  AND (:productName IS NULL OR pri.product.productName LIKE %:productName%)
            """)
   Page<PurchaseRequisitionItemTransfer> filterPurchaseRequisitionTransferItems(@Param("purchaseRequisitionTransferId") Long purchaseRequisitionTransferId, @Param("productName") String productName, Pageable pageable);
}
