package com.osudpotro.posmaster.purchase.purchasecart;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseCartItemAddRequest {
    private Long productId;
    private Long productDetailId;
    private BigDecimal purchasePrice;
    private Integer purchaseQty;
    private BigDecimal purchaseLinePrice;
}
