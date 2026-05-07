package com.osudpotro.posmaster.sale;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SaleItemDto {
    private Long id;
    private Long purchaseId;
    private Long purchaseDetailId;
    private String purchaseBatchNo;
    private String productionBatchNo;
    private String purchaseBarCode;
    private Long productId;
    private String productName;
    private Long productDetailId;
    private String productDetailCode;
    private String productDetailBarCode;
    private String productDetailSku;
    private Long sizeId;
    private String sizeName;
    private Integer saleQty;
    private BigDecimal salePrice;
    private BigDecimal purchasePrice;
    private BigDecimal mrpPrice;
    private Integer currentStock;
}