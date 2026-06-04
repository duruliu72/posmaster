package com.osudpotro.posmaster.offerhub.membership;

import com.osudpotro.posmaster.sale.AmountType;
import lombok.*;

import java.math.BigDecimal;


@Data
public class MembershipDto{
    private Long id;
    private String name;
    private BigDecimal discount;
    private AmountType discountType;
    private Double maxDiscount;
    private Double minPurchaseAmount;
    private Integer lastNMonth;
}
