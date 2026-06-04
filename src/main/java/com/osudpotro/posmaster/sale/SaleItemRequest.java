package com.osudpotro.posmaster.sale;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleItemRequest {
    private Long purchaseId;
    private Long purchaseDetailId;
    private Integer saleQty;
    private BigDecimal salePrice;
    private BigDecimal discount;
    private AmountType discountType;
}
