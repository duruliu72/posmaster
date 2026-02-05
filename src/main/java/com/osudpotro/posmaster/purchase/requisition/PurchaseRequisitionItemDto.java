package com.osudpotro.posmaster.purchase.requisition;

import com.osudpotro.posmaster.product.ProductDetail;
import com.osudpotro.posmaster.product.ProductDetailDto;
import com.osudpotro.posmaster.product.ProductDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseRequisitionItemDto {
    private Long id;
    private ProductDto product;
    private ProductDetailDto productDetail;
    private BigDecimal purchasePrice;
    private BigDecimal mrpPrice;
    private BigDecimal discount;
    private Integer purchaseQty;
    private Integer actualQty;
    private Integer giftOrBonusQty;
    private BigDecimal purchaseLinePrice;
    private BigDecimal actualLinePrice;
    private BigDecimal giftOrBonusLinePrice;
    private Integer addableStatus;
    private ProductDetailDto purchaseProductUnit;
}
