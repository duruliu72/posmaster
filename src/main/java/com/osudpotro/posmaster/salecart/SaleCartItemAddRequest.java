package com.osudpotro.posmaster.salecart;

import lombok.Data;

@Data
public class SaleCartItemAddRequest {
    private Long purchaseId;
    private Long purchaseDetailId;
    private Integer saleQty;
}
