package com.osudpotro.posmaster.purchase.requisition;

public interface PurchaseRequisitionItemReportDTO {
    Long getProductId();
    String getProductName();
    String getCatName();
    String getProductTypeName();
    String getManufacturerName();
    Long getPurchaseProductUnitId();
    String getUnitName();
    Integer getTotalAtomQty();
    Integer getAtomQty();
    Integer getTotalUnitItem();
}
