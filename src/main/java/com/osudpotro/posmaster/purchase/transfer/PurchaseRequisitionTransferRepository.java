package com.osudpotro.posmaster.purchase.transfer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PurchaseRequisitionTransferRepository extends JpaSpecificationExecutor<PurchaseRequisitionTransfer>, JpaRepository<PurchaseRequisitionTransfer, Long> {
    PurchaseRequisitionTransfer findTopByOrderByCreatedAtDesc();

    @Query("SELECT prt FROM PurchaseRequisitionTransfer prt WHERE prt.id = :id")
    Optional<PurchaseRequisitionTransfer> findPurchaseRequisitionById(@Param("id") Long id);

    @Query(value = "SELECT * FROM purchase_requisition_transfers WHERE purchase_invoices LIKE CONCAT('%', :purchase_invoices, '%') AND purchase_requisition_id !=:purchaseRequisitionId limit 1", nativeQuery = true)
    Optional<PurchaseRequisitionTransfer> findFinalPurchaseRequisitionByInvoiceExceptprId(@Param("purchaseRequisitionId") Long purchaseRequisitionId, @Param("purchase_invoices") String purchaseInvoices);
    @Query("SELECT prt FROM PurchaseRequisitionTransfer prt WHERE prt.purchaseRequisition.id = :purchaseRequisitionId AND transferStatus=:transferStatus")
    Optional<PurchaseRequisitionTransfer> findFinalPurchaseRequisitionByPrIDAndTransfer(@Param("purchaseRequisitionId") Long purchaseRequisitionId, @Param("transferStatus") Integer transferStatus);
}
