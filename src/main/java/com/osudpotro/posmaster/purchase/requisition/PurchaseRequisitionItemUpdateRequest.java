package com.osudpotro.posmaster.purchase.requisition;

import lombok.Data;

@Data
public class PurchaseRequisitionItemUpdateRequest {
    private Long productId;
    private Long productDetailId;
    private Double purchasePrice;
    private Integer purchaseQty;
    private Double purchaseLinePrice;
}
