package com.osudpotro.posmaster.offerhub.membership;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MembershipCreateRequest {
    private String name;
    private BigDecimal discount;
    private Boolean isPercentage;
    private Double maxDiscount;
}
