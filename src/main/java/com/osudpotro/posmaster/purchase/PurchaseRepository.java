package com.osudpotro.posmaster.purchase;
import com.osudpotro.posmaster.purchase.checked.CheckedPurchaseRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PurchaseRepository extends JpaSpecificationExecutor<Purchase>, JpaRepository<Purchase, Long> {
    Optional<Purchase> findByCheckedPurchaseRequisition(CheckedPurchaseRequisition checkedPurchaseRequisition);
    Purchase findTopByOrderByPurchaseAtDesc();
}
