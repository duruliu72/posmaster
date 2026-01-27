package com.osudpotro.posmaster.purchase.requisition;

import lombok.Data;

@Data
public class PurchaseRequisitionCreateRequest {
    private String requsitionRef;
    private String purchaseType;
    private Long organizationId;
    private Long branchId;
    private String note;
}
