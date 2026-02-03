package com.osudpotro.posmaster.purchase.requisition;

import com.osudpotro.posmaster.product.ProductDetail;
import com.osudpotro.posmaster.product.ProductDetailDto;
import com.osudpotro.posmaster.product.ProductDto;
import lombok.Data;

@Data
public class PurchaseRequisitionItemDto {
    private Long id;
    private ProductDto product;
    private ProductDetailDto productDetail;
    private Double purchasePrice;
    private Integer purchaseQty;
    private Integer actualQty;
    private Integer giftQty;
    private Double purchaseLinePrice;
    private Double actualLinePrice;
    private Double giftLinePrice;
    private Integer addableStatus;
    private ProductDetailDto purchaseProductUnit;
}
