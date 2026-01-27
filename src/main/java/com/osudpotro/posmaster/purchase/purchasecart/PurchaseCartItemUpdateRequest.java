package com.osudpotro.posmaster.purchase.purchasecart;
import lombok.Data;

@Data
public class PurchaseCartItemUpdateRequest {
    private Double purchasePrice;
    private Long purchaseQty;
    private Double purchaseLinePrice;
}
