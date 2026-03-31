package com.osudpotro.posmaster.inventory;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InventorySummaryDto {
    private Long id;
    private Long invoiceId;
    private Long invoiceDetailId;
    private InvoiceType invoiceType;
    private String purchaseRef;
    private String purchaseBatchNo;
    private String productionBatchNo;
    private LocalDateTime manufactureDate;
    private LocalDateTime expiredDate;
    private Long productId;
    private String productName;
    private String productCode;
    private String productBarCode;
    private Long productDetailId;
    private String productDetailCode;
    private String productDetailBarCode;
    private String productDetailSku;
    private BigDecimal sellPrice;
    private BigDecimal mrpPrice;
    private BigDecimal purchasePrice;
    private Long sizeId;
    private String sizeName;
    private Long organizationId;
    private String organizationName;
    private Long branchId;
    private String branchName;
    private Long warehouseId;
    private String warehouseName;
    private Long supplierId;
    private String supplierName;
    private LocalDateTime invoiceDate;
    private Integer stockIn;
    private Integer stockOut;
    private LocalDateTime createdAt;
}
