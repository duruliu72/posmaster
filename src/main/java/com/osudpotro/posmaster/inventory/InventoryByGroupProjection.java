package com.osudpotro.posmaster.inventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface InventoryByGroupProjection {
    Long getProductId();
    String getProductName();
    String getProductCode();
    String getProductBarCode();
    Long getProductTypeId();
    String getProductTypeName();
    Long getManufacturerId();
    String getManufacturerName();
    Long getProductDetailId();
    String getProductDetailCode();
    String getProductDetailBarCode();
    String getProductDetailSku();
    Long getBranchId();
    String getBranchName();
    Long getSizeId();
    String getSizeName();
    Integer getTotalStockIn();
    Integer getTotalStockOut();
    Integer getCurrentStock();
    BigDecimal getLastPurchasePrice();
    BigDecimal getLastMrpPrice();
    BigDecimal getLastSellPrice();
    LocalDateTime getLastUpdatedAt();
}