package com.osudpotro.posmaster.purchase.purchasecart;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseCartDto{
    private Long id;
    private String name;
    private Double totalPrice;
    private List<PurchaseCartItemDto> items = new ArrayList<>();
}
