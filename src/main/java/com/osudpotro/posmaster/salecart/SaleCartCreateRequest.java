package com.osudpotro.posmaster.salecart;

import lombok.Data;

@Data
public class SaleCartCreateRequest {
    private String email;
    private String mobile;
    private SaleCartItemAddRequest cartItem;
}
