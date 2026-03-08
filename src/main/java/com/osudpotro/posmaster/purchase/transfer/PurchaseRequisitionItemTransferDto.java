package com.osudpotro.posmaster.purchase.transfer;


import com.osudpotro.posmaster.product.ProductDetailDto;
import com.osudpotro.posmaster.product.ProductDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionItem;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseRequisitionItemTransferDto {
    private Long id;
    private PurchaseRequisitionTransfer purchaseRequisitionTransfer;
    private PurchaseRequisitionItem purchaseRequisitionItem;
    private ProductDto product;
    private ProductDetailDto productDetail;
    private BigDecimal purchasePrice;
    private BigDecimal mrpPrice;
    private BigDecimal discount;
    private Integer purchaseQty;
    private Integer giftOrBonusQty;
    private BigDecimal purchaseLinePrice;
    private BigDecimal giftOrBonusLinePrice;
    private ProductDetailDto purchaseProductUnit;
}
