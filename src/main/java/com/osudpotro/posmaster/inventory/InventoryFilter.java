package com.osudpotro.posmaster.inventory;

import lombok.Data;

@Data
public class InventoryFilter {
    private String searchKey;
    private String productName;
    private String purchaseBatchNo;
    private String productionBatchNo;
    private String purchaseBarCode;
    private Long branchId;
}
