package com.osudpotro.posmaster.salecart;

import lombok.Data;
@Data
public class SaleCartItemDto {
  private Long id;
  private Long purchaseId;
  private Long purchaseDetailId;
  private Integer saleQty;
}
