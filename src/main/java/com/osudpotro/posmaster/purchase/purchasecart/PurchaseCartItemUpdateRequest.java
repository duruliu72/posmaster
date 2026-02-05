package com.osudpotro.posmaster.purchase.purchasecart;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseCartItemUpdateRequest {
    private Double purchasePrice;
    private Integer purchaseQty;
    private BigDecimal purchaseLinePrice;
}
