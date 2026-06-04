package com.osudpotro.posmaster.salecart;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleCartCreateRequest {
    private String email;
    private String mobile;
    private BigDecimal overallDiscount;
    private SaleCartItemAddRequest cartItem;
}
