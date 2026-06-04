package com.osudpotro.posmaster.offerhub.membership;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MembershipUpdateRequest {
    private String name;
    private BigDecimal discount;
    private Boolean isPercentage;
    private Double maxDiscount;
    private Double minPurchaseAmount;
    private Integer lastNMonth;
}
