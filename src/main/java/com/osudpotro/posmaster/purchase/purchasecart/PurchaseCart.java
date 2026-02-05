package com.osudpotro.posmaster.purchase.purchasecart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "purchase_carts")
public class PurchaseCart extends BaseEntity {
    private String name;
    @Column(precision = 15, scale = 2)
    private BigDecimal totalPrice;
    @JsonIgnore
    @OneToMany(mappedBy = "purchaseCart", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PurchaseCartItem> items = new ArrayList<>();
    public PurchaseCartItem addPurchaseCartItem() {
        return null;
    }
}
