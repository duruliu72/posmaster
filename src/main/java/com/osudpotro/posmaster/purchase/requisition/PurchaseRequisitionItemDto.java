package com.osudpotro.posmaster.purchase.requisition;

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
    private Double purchaseLinePrice;
}
