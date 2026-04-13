package com.osudpotro.posmaster.sale;

import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.product.ProductDetail;
import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@Entity
@Table(name = "dispatch_items", uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "sale_id",
                "purchase_id",
                "purchase_details_id",
                "product_id",
                "product_detail_id"
        }
))
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id")
    private Sale sale;
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
    private Integer saleQty;
    private BigDecimal salePrice;
//    offer_id
//            discountType
//    discountValue
//            offerStartDate
//    offerEndDate
//            promotion_Id
//    promotionType
//            promotionValue
//    promoStartDate
//            promoEndDate
}
