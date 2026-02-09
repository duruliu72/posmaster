package com.osudpotro.posmaster.purchase.requisition;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseRequisitionItemAddRequest {
    private Long productId;
    private Long productDetailId;
    private Integer purchaseQty;
    private Integer actualQty;
    private BigDecimal purchasePrice;
    private BigDecimal mrpPrice;
    private Integer giftOrBonusQty;
    private Integer addableStatus;
}
