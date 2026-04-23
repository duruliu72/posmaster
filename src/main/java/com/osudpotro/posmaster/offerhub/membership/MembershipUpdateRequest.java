package com.osudpotro.posmaster.offerhub.membership;

import lombok.Data;

@Data
public class MembershipUpdateRequest {
    private String name;
    private Double discount;
    private Boolean isPercentage;
    private Double maxDiscount;
}
