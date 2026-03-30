package com.osudpotro.posmaster.dispatch;

import com.osudpotro.posmaster.purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DispatchRepository extends JpaSpecificationExecutor<Dispatch>, JpaRepository<Dispatch, Long> {
    Dispatch findTopByOrderByDispatchAtDesc();
}
