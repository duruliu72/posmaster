package com.osudpotro.posmaster.dispatch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DispatchRepository extends JpaSpecificationExecutor<Dispatch>, JpaRepository<Dispatch, Long> {
    boolean existsByDispatchRef(String dispatchRef);
    boolean existsByDispatchInvoice(String dispatchInvoice);
    Dispatch findTopByOrderByCreatedAtDesc();
    Dispatch findTopByOrderByDispatchInvoiceDesc();
}
