package com.osudpotro.posmaster.purchase.check;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface FinalPurchaseRequisitionItemRepository extends JpaSpecificationExecutor<FinalPurchaseRequisitionItem>, JpaRepository<FinalPurchaseRequisitionItem, Long> {
   Optional<FinalPurchaseRequisitionItem> findByFinalPurchaseRequisitionIdAndPurchaseRequisitionItemId( Long finalPurchaseRequisitionId, Long purchaseRequisitionItemId);
}
