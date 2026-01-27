package com.osudpotro.posmaster.purchase.purchasecart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osudpotro.posmaster.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "purchase_carts")
public class PurchaseCart extends BaseEntity {
    private String name;
    private Double totalPrice;
    @JsonIgnore
    @OneToMany(mappedBy = "purchaseCart", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PurchaseCartItem> items = new ArrayList<>();
    public PurchaseCartItem addPurchaseCartItem() {
        return null;
    }
}
