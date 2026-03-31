package com.osudpotro.posmaster.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventorySummaryGroupDto {
    // private BigDecimal totalSellPrice;
//    private BigDecimal totalMrpPrice;
//    private BigDecimal totalPurchasePrice;
//    private Long organizationId;
//    private String organizationName;
//    private Long warehouseId;
//    private String warehouseName;
    private Long productId;
    private String productName;
    private String productCode;
    private String productBarCode;
    private Long productDetailId;
    private String productDetailCode;
    private String productDetailBarCode;
    private String productDetailSku;
    private Long sizeId;
    private String sizeName;
    private Long branchId;
    private String branchName;
    private Long totalStockIn;
    private Long totalStockOut;
    private Long currentStock;
}


//Type mapping reference for HQL:
//Entity Field Type	SUM() Return Type
//Integer / int	Long
//Long / long	Long
//BigInteger	BigInteger
//BigDecimal	BigDecimal
//Double / double	Double