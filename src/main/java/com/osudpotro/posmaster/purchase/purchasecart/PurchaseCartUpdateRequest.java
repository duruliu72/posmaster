package com.osudpotro.posmaster.purchase.purchasecart;

import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class PurchaseCartUpdateRequest {
    private String name;
    private Set<PurchaseCartItemAddRequest> items = new LinkedHashSet<>();
}
