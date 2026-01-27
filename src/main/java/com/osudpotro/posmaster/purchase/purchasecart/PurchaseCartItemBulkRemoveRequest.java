package com.osudpotro.posmaster.purchase.purchasecart;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseCartItemBulkRemoveRequest {
    private List<Long> purchaseCartItemIds;
}
