package com.osudpotro.posmaster.purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PurchaseDetailRepository extends JpaSpecificationExecutor<PurchaseDetail>, JpaRepository<PurchaseDetail, Long> {
    PurchaseDetail findTopByOrderByAddedAtDesc();
}
