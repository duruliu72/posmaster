package com.osudpotro.posmaster.tms.goodsontrip;

import com.osudpotro.posmaster.purchase.transfer.PurchaseRequisitionTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface GoodsOnTripRepository extends JpaSpecificationExecutor<GoodsOnTrip>, JpaRepository<GoodsOnTrip, Long> {
    Optional<GoodsOnTrip> findByPurchaseRequisitionTransfer(PurchaseRequisitionTransfer purchaseRequisitionTransfer);
    GoodsOnTrip findTopByOrderByCreatedAtDesc();
}
