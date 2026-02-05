package com.osudpotro.posmaster.purchase.purchasecart;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseCartWithItemPageResponse {
    private Long id;
    private String name;
    private BigDecimal totalPrice;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private List<PurchaseCartItemDto> content = new ArrayList<>();
}
