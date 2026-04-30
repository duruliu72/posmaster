package com.osudpotro.posmaster.salecart;

import lombok.Data;

@Data
public class UpdateSaleCartItemRequest {
    private Long purchaseId;
    private Long purchaseDetailId;
    private Integer saleQty;
}
