package com.osudpotro.posmaster.purchase.requisition;

import lombok.Data;

@Data
public class PurchaseRequisitionUpdateRequest {
    private String purchaseType;
    private Long organizationId;
    private Long branchId;
    private Integer requisitionStatus;
    private String note;
}
