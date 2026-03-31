package com.osudpotro.posmaster.inventory;

import com.osudpotro.posmaster.branch.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventorySummaryRepository extends JpaSpecificationExecutor<InventorySummary>, JpaRepository<InventorySummary, Long> {
    List<InventorySummary> findAllByBranch(Branch branch);
    @Query("SELECT new com.osudpotro.posmaster.inventory.InventorySummaryGroupDto(" +
            "i.product.id, " +
            "i.product.productName as productName, " +
            "i.product.productCode as productCode, " +
            "i.product.productBarCode as productBarCode, " +
            "i.productDetail.id, " +
            "i.productDetail.productDetailCode, " +
            "i.productDetail.productDetailBarCode, " +
            "i.productDetail.productDetailSku, " +
            "i.productDetail.size.id, " +
            "i.productDetail.size.name, " +
            "i.branch.id, " +
            "i.branch.name, " +
            "COALESCE(SUM(i.stockIn), 0), " +
            "COALESCE(SUM(i.stockOut), 0), " +
            "COALESCE(SUM(i.stockIn), 0) - COALESCE(SUM(i.stockOut), 0)) " +
            "FROM InventorySummary i " +
            "WHERE i.branch.id = :branchId " +
            "GROUP BY i.product.id,i.product.productName,i.product.productCode,i.product.productBarCode," +
            "i.productDetail.id,i.productDetail.productDetailCode,i.productDetail.productDetailBarCode,i.productDetail.productDetailSku," +
            "i.productDetail.size.id,i.productDetail.size.name," +
            "i.branch.id,i.branch.name,i.product.id,i.productDetail.id")
    List<InventorySummaryGroupDto> findAllGroupInventorySummary(@Param("branchId") Long branchId);
    @Query("SELECT new com.osudpotro.posmaster.inventory.InventorySummaryGroupDto(" +
            "i.product.id, " +
            "i.product.productName as productName, " +
            "i.product.productCode as productCode, " +
            "i.product.productBarCode as productBarCode, " +
            "i.productDetail.id, " +
            "i.productDetail.productDetailCode, " +
            "i.productDetail.productDetailBarCode, " +
            "i.productDetail.productDetailSku, " +
            "i.productDetail.size.id, " +
            "i.productDetail.size.name, " +
            "i.branch.id, " +
            "i.branch.name, " +
            "COALESCE(SUM(i.stockIn), 0), " +
            "COALESCE(SUM(i.stockOut), 0), " +
            "COALESCE(SUM(i.stockIn), 0) - COALESCE(SUM(i.stockOut), 0)) " +
            "FROM InventorySummary i " +
            "GROUP BY i.product.id,i.product.productName,i.product.productCode,i.product.productBarCode," +
            "i.productDetail.id,i.productDetail.productDetailCode,i.productDetail.productDetailBarCode,i.productDetail.productDetailSku," +
            "i.productDetail.size.id,i.productDetail.size.name," +
            "i.branch.id,i.branch.name")
    Page<InventorySummaryGroupDto> findAllGroupInventorySummaryPage(Pageable pageable);
    @Query("SELECT new com.osudpotro.posmaster.inventory.InventorySummaryGroupDto(" +
            "i.product.id, " +
            "i.product.productName as productName, " +
            "i.product.productCode as productCode, " +
            "i.product.productBarCode as productBarCode, " +
            "i.productDetail.id, " +
            "i.productDetail.productDetailCode, " +
            "i.productDetail.productDetailBarCode, " +
            "i.productDetail.productDetailSku, " +
            "i.productDetail.size.id, " +
            "i.productDetail.size.name, " +
            "i.branch.id, " +
            "i.branch.name, " +
            "COALESCE(SUM(i.stockIn), 0), " +
            "COALESCE(SUM(i.stockOut), 0), " +
            "COALESCE(SUM(i.stockIn), 0) - COALESCE(SUM(i.stockOut), 0)) " +
            "FROM InventorySummary i " +
            "GROUP BY i.product.id,i.product.productName,i.product.productCode,i.product.productBarCode," +
            "i.productDetail.id,i.productDetail.productDetailCode,i.productDetail.productDetailBarCode,i.productDetail.productDetailSku," +
            "i.productDetail.size.id,i.productDetail.size.name," +
            "i.branch.id,i.branch.name")
    Page<InventorySummaryGroupDto> findAllGroupInventorySummary(Specification<InventorySummary> spec, Pageable pageable);

    @Query("SELECT i.product.id as productId, " +
            "i.product.productName as productName, " +
            "i.product.productCode as productCode, " +
            "i.product.productBarCode as productBarCode, " +
            "i.productDetail.id as productDetailId, " +
            "SUM(i.stockIn) as totalStockIn, " +
            "SUM(i.stockOut) as totalStockOut, " +
            "SUM(i.stockIn) - SUM(i.stockOut) as currentStock " +
            "FROM InventorySummary i " +
            "GROUP BY i.product.id,i.product.productName, i.productDetail.id")
    Page<InventorySummaryGroupProjection> findAllGroupInventorySummaryByProjection(Specification<InventorySummary> spec, Pageable pageable);
}
