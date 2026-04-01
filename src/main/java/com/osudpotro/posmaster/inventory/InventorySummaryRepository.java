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
            "i.branch.id, " +
            "i.branch.name, " +
            "i.productDetail.size.id, " +
            "i.productDetail.size.name, " +
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
            "i.branch.id, " +
            "i.branch.name, " +
            "i.productDetail.size.id, " +
            "i.productDetail.size.name, " +
            "COALESCE(SUM(i.stockIn), 0), " +
            "COALESCE(SUM(i.stockOut), 0), " +
            "COALESCE(SUM(i.stockIn), 0) - COALESCE(SUM(i.stockOut), 0)) " +
            "FROM InventorySummary i " +
            "GROUP BY i.product.id,i.product.productName,i.product.productCode,i.product.productBarCode," +
            "i.productDetail.id,i.productDetail.productDetailCode,i.productDetail.productDetailBarCode,i.productDetail.productDetailSku," +
            "i.productDetail.size.id,i.productDetail.size.name," +
            "i.branch.id,i.branch.name,i.product.id,i.productDetail.id")
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
            "i.branch.id, " +
            "i.branch.name, " +
            "i.productDetail.size.id, " +
            "i.productDetail.size.name, " +
            "COALESCE(SUM(i.stockIn), 0), " +
            "COALESCE(SUM(i.stockOut), 0), " +
            "COALESCE(SUM(i.stockIn), 0) - COALESCE(SUM(i.stockOut), 0)) " +
            "FROM InventorySummary i " +
            "GROUP BY i.product.id,i.product.productName,i.product.productCode,i.product.productBarCode," +
            "i.productDetail.id,i.productDetail.productDetailCode,i.productDetail.productDetailBarCode,i.productDetail.productDetailSku," +
            "i.productDetail.size.id,i.productDetail.size.name," +
            "i.branch.id,i.branch.name,i.product.id,i.productDetail.id")
    Page<InventorySummaryGroupDto> findAllGroupInventorySummary(Specification<InventorySummary> spec, Pageable pageable);
    @Query("SELECT i.product.id as productId," +
            "i.product.productName as productName," +
            "i.product.productCode as productCode, " +
            "i.product.productBarCode as productBarCode," +
            "i.product.productType.id as productTypeId, " +
            "i.product.productType.name as productTypeName, " +
            "i.product.manufacturer.id as manufacturerId, " +
            "i.product.manufacturer.name as manufacturerName, " +
            "i.productDetail.id as productDetailId, " +
            "i.productDetail.productDetailCode as productDetailCode, " +
            "i.productDetail.productDetailBarCode as productDetailBarCode, " +
            "i.productDetail.productDetailSku as productDetailSku, " +
            "i.productDetail.purchasePrice as lastPurchasePrice, " +
            "i.productDetail.mrpPrice as lastMrpPrice, " +
            "i.productDetail.sellPrice as lastSellPrice, " +
            "i.productDetail.updatedAt as lastUpdatedAt, " +
            "i.branch.id as branchId, " +
            "i.branch.name as branchName, " +
            "i.productDetail.size.id as sizeId, " +
            "i.productDetail.size.name as sizeName, " +
            "COALESCE(SUM(i.stockIn), 0) as totalStockIn, " +
            "COALESCE(SUM(i.stockOut), 0) as totalStockOut, " +
            "COALESCE(SUM(i.stockIn), 0) - COALESCE(SUM(i.stockOut), 0) as currentStock " +
            "FROM InventorySummary i " +
            "WHERE (:branchId IS NULL OR i.branch.id = :branchId) " +
            "GROUP BY " +
            "i.product.id," +
            "i.product.productName," +
            "i.product.productCode," +
            "i.product.productBarCode," +
            "i.product.productType.id," +
            "i.product.productType.name," +
            "i.product.manufacturer.id," +
            "i.product.manufacturer.name," +
            "i.productDetail.id," +
            "i.productDetail.productDetailCode," +
            "i.productDetail.productDetailBarCode," +
            "i.productDetail.productDetailSku," +
            "i.productDetail.purchasePrice," +
            "i.productDetail.mrpPrice," +
            "i.productDetail.sellPrice," +
            "i.productDetail.updatedAt," +
            "i.branch.id,i.branch.name," +
            "i.productDetail.size.id, " +
            "i.productDetail.size.name")
    Page<InventorySummaryGroupProjection> findAllGroupInventorySummaryByProjection(@Param("branchId") Long branchId,Pageable pageable);
}
