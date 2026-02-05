package com.osudpotro.posmaster.purchase.purchasecart;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseCartDto{
    private Long id;
    private String name;
    private BigDecimal totalPrice;
    private List<PurchaseCartItemDto> items = new ArrayList<>();
}
