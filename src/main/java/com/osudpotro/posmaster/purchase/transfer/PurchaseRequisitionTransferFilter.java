package com.osudpotro.posmaster.purchase.transfer;

import lombok.Data;

@Data
public class PurchaseRequisitionTransferFilter {
    private String requsitionRef;
    private String purchaseType;
    private Integer status;
    private Long purchaseRequisitionId;
}
