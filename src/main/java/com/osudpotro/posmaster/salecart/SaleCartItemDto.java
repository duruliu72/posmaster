package com.osudpotro.posmaster.salecart;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleCartItemDto {
  private Long id;
  private Long purchaseId;
  private Long purchaseDetailId;
  private String purchaseBatchNo;
  private String productionBatchNo;
  private String purchaseBarCode;
  private Long productId;
  private String productName;
  private Long sizeId;
  private String sizeName;
  private BigDecimal purchasePrice;
  private BigDecimal sellPrice;
  private BigDecimal mrpPrice;
  private Integer saleQty;
  private Integer currentStock;  // OASIK ✅ ADD THIS
}
