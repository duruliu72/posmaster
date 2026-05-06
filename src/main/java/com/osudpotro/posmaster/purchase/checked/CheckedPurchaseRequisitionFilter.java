package com.osudpotro.posmaster.purchase.checked;

import lombok.Data;

@Data
public class CheckedPurchaseRequisitionFilter {
    private String name;
    private String requsitionRef;
    private String purchaseType;
    private Integer status;
    private Integer checkedStatus;
}
