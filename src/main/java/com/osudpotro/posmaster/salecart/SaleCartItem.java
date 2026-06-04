package com.osudpotro.posmaster.salecart;

import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
@Getter
@Setter
@Entity
@Table(name = "sale_cart_items")
public class SaleCartItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_cart_id")
    private SaleCart saleCart;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_details_id")
    private PurchaseDetail purchaseDetail;
    private Integer saleQty;
    private BigDecimal discount;
}
