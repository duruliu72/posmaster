package com.osudpotro.posmaster.purchase.purchasecart;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.product.ProductDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "purchase_cart_items", uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "purchase_cart_id",
                "product_id",
                "product_detail_id"
        }
))
public class PurchaseCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_cart_id")
    private PurchaseCart purchaseCart;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;
    private Integer purchaseQty;
    //    private Double profit;//    Not for  Calculation
//  %=1,2=amount
//    private int profitIn;
//    private Double sellAmount;//    Not for  Calculation
//    Selling price Should be In diffrent table
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
