package com.osudpotro.posmaster.salecart;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class SaleCartDto {
  private Long id;
  private String email;
  private String mobile;
  private Long branchId;
  private String branchName;
  private BigDecimal overallDiscount;
  private List<SaleCartItemDto> items = new ArrayList<>();
}
