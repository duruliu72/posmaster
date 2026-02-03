package com.osudpotro.posmaster.purchase.requisition;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PurchaseRequisitionItemRepository extends JpaSpecificationExecutor<PurchaseRequisitionItem>, JpaRepository<PurchaseRequisitionItem, Long> {
    @Query("""
                SELECT pri FROM PurchaseRequisitionItem pri
                WHERE pri.purchaseRequisition.id = :purchaseRequisitionId
                  AND pri.product.id = :productId
                  AND pri.productDetail.id = :productDetailId
            """)
    Optional<PurchaseRequisitionItem> findPurchaseRequisitionItem(
            @Param("purchaseRequisitionId") Long purchaseRequisitionId,
            @Param("productId") Long productId,
            @Param("productDetailId") Long productDetailId
    );

    @Query("""
                SELECT pri FROM PurchaseRequisitionItem pri
                WHERE pri.purchaseRequisition.id = :purchaseRequisitionId
                ORDER BY pri.product.manufacturer.id
            """)
    List<PurchaseRequisitionItem> findPurchaseRequisitionItemsList(@Param("purchaseRequisitionId") Long purchaseRequisitionId);
    @Query(value = """
                SELECT pri.product_id,p.product_name,cat.name as cat_name,pt.name as product_type_name,mf.name as manufacturer_name,pri.purchase_product_unit_id,ppvu.name as unit_name,sum(pri.purchase_qty*pd.atom_qty) as total_atom_qty,ppd.atom_qty,CEILING(sum(pri.purchase_qty*pd.atom_qty)/ppd.atom_qty) as total_unit_item
                                                    	FROM public.purchase_requisition_items as pri
                                                    	JOIN products as p on p.id=pri.product_id
                                                    	JOIN categories as cat on cat.id=p.category_id
                                                    	JOIN product_type as pt on pt.id=p.product_type_id
                                                    	JOIN manufacturer as mf on mf.id=p.manufacturer_id
                                                    	JOIN product_detail as pd on pd.id=pri.product_detail_id
                                                    	JOIN product_detail as ppd on ppd.id=p.purchase_product_unit_id
                                                    	JOIN variant_unit as ppvu on ppvu.id= ppd.size_id
                                                    	WHERE pri.purchase_requisition_id=3 group by pri.product_id,p.product_name,cat.name,pt.name,mf.id,mf.name,ppd.id,pri.purchase_product_unit_id,ppvu.name order by mf.id
            """,nativeQuery = true)
    List<PurchaseRequisitionItemReportDTO> findPurchaseRequisitionItemReportList(@Param("purchaseRequisitionId") Long purchaseRequisitionId);

    @Query("""
                SELECT pri FROM PurchaseRequisitionItem pri
                WHERE pri.purchaseRequisition.id = :purchaseRequisitionId
                  AND (:productName IS NULL OR pri.product.productName LIKE %:productName%)
            """)
    Page<PurchaseRequisitionItem> findPurchaseRequisitionItems(@Param("purchaseRequisitionId") Long purchaseRequisitionId, @Param("productName") String productName, Pageable pageable);

    @Transactional
    @Modifying
    @Query("""
                DELETE FROM PurchaseRequisitionItem pri
                WHERE pri.id in :purchaseRequisitionItemIds
                  AND pri.purchaseRequisition.id = :purchaseRequisitionId
            """)
    int removeBulkPurchaseRequisitionItem(@Param("purchaseRequisitionId") Long purchaseRequisitionId, @Param("purchaseRequisitionItemIds") List<Long> purchaseRequisitionItemIds);

    @Transactional
    @Modifying
    @Query("""
                update PurchaseRequisitionItem pri
                set pri.addableStatus = :addableStatus
                WHERE pri.id in :purchaseRequisitionItemIds
                  AND pri.purchaseRequisition.id = :purchaseRequisitionId
            """)
//    @Query("update Organization o set o.status = :status where o.id in :ids")
    int updateBulkForAddableItem(@Param("purchaseRequisitionId") Long purchaseRequisitionId, @Param("purchaseRequisitionItemIds") List<Long> purchaseRequisitionItemIds, @Param("addableStatus") Integer addableStatus);
}
