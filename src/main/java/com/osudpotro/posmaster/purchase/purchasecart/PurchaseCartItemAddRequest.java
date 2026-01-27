package com.osudpotro.posmaster.purchase.purchasecart;
import lombok.Data;

@Data
public class PurchaseCartItemAddRequest {
    private Long productId;
    private Long productDetailId;
    private Double purchasePrice;
    private Long purchaseQty;
    private Double purchaseLinePrice;
}
