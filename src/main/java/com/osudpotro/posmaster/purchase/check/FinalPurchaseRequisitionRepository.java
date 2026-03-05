package com.osudpotro.posmaster.purchase.check;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FinalPurchaseRequisitionRepository extends JpaSpecificationExecutor<FinalPurchaseRequisition>, JpaRepository<FinalPurchaseRequisition, Long> {
    boolean existsByRequsitionRef(String requsitionRef);

    boolean existsByPurchaseInvoices(String purchaseInvoices);

    @Transactional
    @Modifying
    @Query("update FinalPurchaseRequisition pr set pr.status = :status where pr.id in :ids")
    int deleteBulkPurchaseRequisition(@Param("ids") List<Long> ids, @Param("status") Long status);

    FinalPurchaseRequisition findTopByOrderByCreatedAtDesc();

    @Query("SELECT pr FROM FinalPurchaseRequisition pr WHERE pr.id = :id")
    Optional<FinalPurchaseRequisition> findPurchaseRequisitionById(@Param("id") Long id);

    @Query(value = "SELECT * FROM final_purchase_requisitions WHERE purchase_invoices LIKE CONCAT('%', :purchase_invoices, '%') AND purchase_requisition_id !=:purchaseRequisitionId limit 1", nativeQuery = true)
    Optional<FinalPurchaseRequisition> findFinalPurchaseRequisitionByInvoiceExceptprId(@Param("purchaseRequisitionId") Long purchaseRequisitionId, @Param("purchase_invoices") String purchaseInvoices);
    @Query("SELECT fpr FROM FinalPurchaseRequisition fpr WHERE fpr.purchaseRequisition.id = :purchaseRequisitionId AND transferStatus=:transferStatus")
    Optional<FinalPurchaseRequisition> findFinalPurchaseRequisitionByPrIDAndTransfer(@Param("purchaseRequisitionId") Long purchaseRequisitionId, @Param("transferStatus") Integer transferStatus);
}
