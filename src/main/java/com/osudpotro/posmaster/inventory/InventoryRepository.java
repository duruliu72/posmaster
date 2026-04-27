package com.osudpotro.posmaster.inventory;

import com.osudpotro.posmaster.branch.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryRepository extends JpaSpecificationExecutor<Inventory>, JpaRepository<Inventory, Long> {
    List<Inventory> findAllByBranch(Branch branch);

    @Query("SELECT " +
            "i.purchase.id as purchaseId," +
            "i.purchaseDetail.id as purchaseDetailId," +
            "i.purchaseBatchNo as purchaseBatchNo," +
            "i.productionBatchNo as productionBatchNo," +
            "i.product.id as productId," +
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
            "COALESCE(SUM(i.stockIn), 0) - COALESCE(SUM(i.stockOut), 0) as currentStock, " +
            "i.createdAt as createdAt " +
            "FROM Inventory i " +
            "WHERE (:productId IS NULL OR i.product.id = :productId) " +
            "AND (:productDetailId IS NULL OR i.productDetail.id = :productDetailId) " +
            "GROUP BY " +
            "i.purchase.id," +
            "i.purchaseDetail.id," +
            "i.purchaseBatchNo," +
            "i.productionBatchNo," +
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
            "i.productDetail.size.name," +
            "i.createdAt")
    List<InventoryByBatchNo> getInvListByBatch(@Param("productId") Long productId, @Param("productDetailId") Long productDetailId);

    @Query("SELECT " +
            "i.purchaseBatchNo as purchaseBatchNo," +
            "i.productionBatchNo as productionBatchNo," +
            "i.product.id as productId," +
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
            "FROM Inventory i " +
            "WHERE (:branchId IS NULL OR i.branch.id = :branchId) " +
            "AND (:searchKey IS NULL OR LOWER(i.product.productName) LIKE LOWER(CONCAT('%', :searchKey, '%'))) " +
            "GROUP BY " +
            "i.purchaseBatchNo," +
            "i.productionBatchNo," +
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
    Page<InventoryByBatchNo> filterInvGroupBatchByBranch(@Param("branchId") Long branchId, @Param("searchKey") String searchKey, Pageable pageable);

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
            "FROM Inventory i " +
            "WHERE (:branchId IS NULL OR i.branch.id = :branchId) " +
            "AND (:searchKey IS NULL OR LOWER(i.product.productName) LIKE LOWER(CONCAT('%', :searchKey, '%'))) " +
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
    Page<InventoryByProductDetail> filterInvGroupProductDetailByBranch(@Param("branchId") Long branchId, @Param("searchKey") String searchKey, Pageable pageable);
}
