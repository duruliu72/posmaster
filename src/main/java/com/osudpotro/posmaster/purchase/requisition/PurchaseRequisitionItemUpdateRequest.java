package com.osudpotro.posmaster.purchase.requisition;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseRequisitionItemUpdateRequest {
    private Long productId;
    private Long productDetailId;
    private BigDecimal purchasePrice;
    private BigDecimal mrpPrice;
    private Integer purchaseQty;
    private Integer actualQty;
    private Integer giftOrBonusQty;
    private Integer addableStatus;
}
