package com.osudpotro.posmaster.purchase.checked;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CheckedPurchaseRequisitionRepository extends JpaSpecificationExecutor<CheckedPurchaseRequisition>, JpaRepository<CheckedPurchaseRequisition, Long> {
    @Query(value = "SELECT * FROM checked_purchase_requisitions WHERE purchase_invoices LIKE CONCAT('%', :purchase_invoices, '%') AND (purchase_requisition_id !=:purchaseRequisitionId OR checked_status=2) limit 1", nativeQuery = true)
    Optional<CheckedPurchaseRequisition> findCheckedPurchaseRequisitionByInvoiceExceptprId(@Param("purchaseRequisitionId") Long purchaseRequisitionId, @Param("purchase_invoices") String purchaseInvoices);
    @Query("SELECT cpr FROM CheckedPurchaseRequisition cpr WHERE cpr.purchaseRequisition.id = :purchaseRequisitionId AND checkedStatus=:checkedStatus AND checkByAdmin IS NULL")
    Optional<CheckedPurchaseRequisition> findCheckPurchaseRequisitionByPrIDAndCheckedStatus(@Param("purchaseRequisitionId") Long purchaseRequisitionId, @Param("checkedStatus") Integer checkedStatus);
}
