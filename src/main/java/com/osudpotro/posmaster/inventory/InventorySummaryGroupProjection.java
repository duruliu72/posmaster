package com.osudpotro.posmaster.inventory;

public interface InventorySummaryGroupProjection {
    Long getProductId();
    String getProductName();
    String getProductCode();
    String getProductBarCode();
    Long getProductDetailId();
    Integer getTotalStockIn();
    Integer getTotalStockOut();
    Integer getCurrentStock();
}