package com.osudpotro.posmaster.offerhub.membership;

import lombok.*;


@Data
public class MembershipDto{
    private Long id;
    private String name;
    private Double discount;
    private Double maxDiscount;
    private Boolean isPercentage;
}
