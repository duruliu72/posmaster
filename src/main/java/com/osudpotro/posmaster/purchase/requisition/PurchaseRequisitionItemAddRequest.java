package com.osudpotro.posmaster.purchase.requisition;

import lombok.Data;

@Data
public class PurchaseRequisitionItemAddRequest {
    private Long productId;
    private Long productDetailId;
    private Double purchasePrice;
    private Integer purchaseQty;
    private Integer giftQty;
    private Double purchaseLinePrice;
}
