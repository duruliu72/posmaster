package com.osudpotro.posmaster.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InventorySummaryRepository extends JpaSpecificationExecutor<InventorySummary>, JpaRepository<InventorySummary, Long> {
}
