package com.osudpotro.posmaster.purchase.requisition;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequisitionItemBulkRequest {
    private List<Long> purchaseRequisitionItemIds;
}
