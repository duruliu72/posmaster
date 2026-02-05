package com.osudpotro.posmaster.purchase.purchasecart;

import com.osudpotro.posmaster.product.ProductDetailDto;
import com.osudpotro.posmaster.product.ProductDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseCartItemDto {
    private Long id;
    private ProductDto product;
    private ProductDetailDto productDetail;
    private BigDecimal purchasePrice;
    private Integer purchaseQty;
    private BigDecimal purchaseLinePrice;
}
