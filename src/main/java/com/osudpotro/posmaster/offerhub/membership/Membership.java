package com.osudpotro.posmaster.offerhub.membership;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.sale.AmountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "memberships")
public class Membership extends BaseEntity {
    private String name;
    @Column(name = "code", nullable = true, unique = true, length = 50)
    private String code;
    private BigDecimal discount;
    @Enumerated(EnumType.STRING)
    private AmountType discountType;
    private Double maxDiscount;
    private Double minPurchaseAmount;
    private Integer lastNMonth;
}
