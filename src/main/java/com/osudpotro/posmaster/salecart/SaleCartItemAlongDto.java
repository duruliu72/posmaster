package com.osudpotro.posmaster.salecart;

import lombok.Data;

@Data
public class SaleCartItemAlongDto extends SaleCartItemDto {
  private String email;
  private String mobile;
}
