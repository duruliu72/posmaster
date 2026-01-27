package com.osudpotro.posmaster.purchase.purchasecart;

import com.osudpotro.posmaster.product.ProductDetailDto;
import com.osudpotro.posmaster.product.ProductDto;
import lombok.Data;

@Data
public class PurchaseCartItemDto {
    private Long id;
    private ProductDto product;
    private ProductDetailDto productDetail;
    private Double purchasePrice;
    private Long purchaseQty;
    private Double purchaseLinePrice;
}
