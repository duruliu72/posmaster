package com.osudpotro.posmaster.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PurchaseRepository extends JpaSpecificationExecutor<Purchase>, JpaRepository<Purchase, Long> {
}
