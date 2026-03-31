package com.osudpotro.posmaster.purchase.requisition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PurchaseRequisitionRepository extends JpaSpecificationExecutor<PurchaseRequisition>, JpaRepository<PurchaseRequisition, Long> {
    boolean existsByRequsitionRef(String requsitionRef);
    boolean existsByPurchaseInvoices(String purchaseInvoices);
    @Transactional
    @Modifying
    @Query("update PurchaseRequisition pr set pr.status = :status where pr.id in :ids")
    int deleteBulkPurchaseRequisition(@Param("ids") List<Long> ids, @Param("status") Long status);
    PurchaseRequisition findTopByOrderByCreatedAtDesc();
    @Query("SELECT pr FROM PurchaseRequisition pr WHERE pr.id = :id")
    Optional<PurchaseRequisition> findPurchaseRequisitionById(@Param("id") Long id);

//    @Query(value = "SELECT * FROM purchase_requisitions WHERE purchase_invoices LIKE CONCAT('%', :purchase_invoices, '%') AND id !=:id limit 1",nativeQuery = true)
    @Query(value = "SELECT * FROM purchase_requisitions WHERE " +
            "(purchase_invoices LIKE CONCAT('%', :purchase_invoices, '%') OR " +
            "purchase_invoices LIKE CONCAT('%', :purchase_invoices, '%')) " +
            "AND id != :id LIMIT 1", nativeQuery = true)
    Optional<PurchaseRequisition> findPurchaseRequisitionByInvoice(@Param("id") Long id,@Param("purchase_invoices") String purchaseInvoices);
}
