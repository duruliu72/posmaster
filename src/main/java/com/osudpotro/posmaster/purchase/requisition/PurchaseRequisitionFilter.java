package com.osudpotro.posmaster.purchase.requisition;

import lombok.Data;

@Data
public class PurchaseRequisitionFilter {
    private String requsitionRef;
    private String purchaseType;
    private Integer status;
}
