package com.osudpotro.posmaster.dispatch;

import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.product.ProductDetail;
import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "dispatch_items", uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "dispatch_id",
                "purchase_id",
                "purchase_details_id",
                "product_id",
                "product_detail_id"
        }
))
public class DispatchItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dispatch_id")
    private Dispatch dispatch;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_details_id")
    private PurchaseDetail purchaseDetail;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;
    //requestedQty is at time of requested by requested branch
    private Integer requestedQty;
    //updatedQty is at time of updated by acceptor branch
    private Integer updatedQty;
    //dispatchQty is at time of send by acceptor branch
    private Integer dispatchQty;
    private Boolean isRequestedItem;
}
