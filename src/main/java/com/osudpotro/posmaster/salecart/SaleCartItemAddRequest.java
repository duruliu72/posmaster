package com.osudpotro.posmaster.salecart;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleCartItemAddRequest {
    private Long purchaseId;
    private Long purchaseDetailId;
    private Integer saleQty;
    private BigDecimal discount;
}
