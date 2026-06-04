package com.osudpotro.posmaster.offerhub.membership;

import lombok.*;

import java.math.BigDecimal;


@Data
public class MembershipDto{
    private Long id;
    private String name;
    private BigDecimal discount;
    private Double maxDiscount;
    private Boolean isPercentage;
}
