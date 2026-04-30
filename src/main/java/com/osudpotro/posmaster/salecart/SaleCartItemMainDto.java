package com.osudpotro.posmaster.salecart;

import lombok.Data;

@Data
public class SaleCartItemMainDto {
  private Long id;
  private Long saleCartId;
  private Long purchaseId;
  private Long purchaseDetailId;
  private Integer saleQty;
}
