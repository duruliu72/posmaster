package com.osudpotro.posmaster.purchase.checked;


import com.osudpotro.posmaster.product.ProductDetailDto;
import com.osudpotro.posmaster.product.ProductDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionItemDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CheckedPurchaseRequisitionItemDto {
    private Long id;
    private CheckedPurchaseRequisitionDto checkedPurchaseRequisition;
    private Long checkedPurchaseRequisitionId;
    private PurchaseRequisitionDto purchaseRequisition;
    private Long purchaseRequisitionId;
    private PurchaseRequisitionItemDto purchaseRequisitionItem;
    private Long purchaseRequisitionItemId;
    private ProductDto product;
    private ProductDetailDto productDetail;
    private BigDecimal purchasePrice;
    private BigDecimal mrpPrice;
    private BigDecimal discount;
    private Integer purchaseQty;
    private Integer giftOrBonusQty;
    private BigDecimal purchaseLinePrice;
    private BigDecimal giftOrBonusLinePrice;
    private String productionBatchNo;
    private LocalDateTime manufactureDate;
    private LocalDateTime expiredDate;
    private Boolean isAddedToInventory;
}
