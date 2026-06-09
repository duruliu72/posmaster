package com.osudpotro.posmaster.purchase;

import com.osudpotro.posmaster.product.ProductDetailDto;
import com.osudpotro.posmaster.product.ProductDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionItemDto;
import com.osudpotro.posmaster.user.UserPlainDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PurchaseDetailDto {
    private Long id;
    private PurchaseDto purchase;
    private PurchaseRequisitionDto purchaseRequisition;
    private PurchaseRequisitionItemDto purchaseRequisitionItem;
    private ProductDto product;
    private ProductDetailDto productDetail;
    private BigDecimal purchasePrice;
    private BigDecimal mrpPrice;
    private BigDecimal purchaseDiscount;
    private Integer purchaseQty;
    private Integer giftOrBonusQty;
    private Integer atomQty;
    private String purchaseBarCode;
    private String productionBatchNo;
    private LocalDateTime manufactureDate;
    private LocalDateTime expiredDate;
    private UserPlainDto addedBy;
    private UserPlainDto updatedBy;
    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;
}
