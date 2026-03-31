package com.osudpotro.posmaster.purchase.requisition;

import lombok.Data;

@Data
public class PurchaseRequisitionCreateRequest {
    private String purchaseType;
    private String note;
}
