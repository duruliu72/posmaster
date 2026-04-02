package com.osudpotro.posmaster.purchase.checked;

import lombok.Data;

@Data
public class UpdateFromBranchRequest {
    private Long checkedPurchaseRequisitionItemId;
    private String productionBatchNo;
    private String manufactureDate;
    private String expiredDate;
}
