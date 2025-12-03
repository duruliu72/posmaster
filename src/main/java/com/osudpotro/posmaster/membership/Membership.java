package com.osudpotro.posmaster.membership;

import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "memberships")
public class Membership extends BaseEntity {
    private String name;
    private Double discount;
    private int isPercentage;
    private Double maxDiscount;

}
