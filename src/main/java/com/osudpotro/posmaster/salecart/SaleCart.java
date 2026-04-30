package com.osudpotro.posmaster.salecart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.user.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sale_carts",indexes = {
        @Index(name = "idx_sale_cart_email", columnList = "email"),
        @Index(name = "idx_sale_cart_mobile", columnList = "mobile"),
})
public class SaleCart extends BaseEntity {
    @Column(unique = true, nullable = true)
    private String email;
    @Column(unique = true, nullable = true)
    private String mobile;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;
    @Enumerated(EnumType.STRING)
    private UserType userType=UserType.CUSTOMER;
    @JsonIgnore
    @OneToMany(mappedBy = "saleCart", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SaleCartItem> items = new ArrayList<>();
}
