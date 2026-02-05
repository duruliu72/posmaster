package com.osudpotro.posmaster.purchase.requisition;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.product.ProductDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "purchase_requisition_items", uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "purchase_requisition_id",
                "product_id",
                "product_detail_id"
        }
))
public class PurchaseRequisitionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_requisition_id")
    private PurchaseRequisition purchaseRequisition;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;
    @Column(precision = 38, scale = 18)
    private BigDecimal purchasePrice;
    @Column(precision = 38, scale = 18)
    private BigDecimal mrpPrice;
    @Column(precision = 38, scale = 18)
    private BigDecimal discount;
    private Integer purchaseQty;
    private Integer actualQty;
    private Integer giftOrBonusQty;
    //1 or Null=For Add,2=Addable,3=Added
    private Integer addableStatus;
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_product_unit_id")
    private ProductDetail purchaseProductUnit;

    public BigDecimal getQty() {
        return BigDecimal.valueOf(purchaseQty);
    }

    public BigDecimal getDiscountPrice() {
        if (mrpPrice == null || purchasePrice == null) {
            return BigDecimal.ZERO;
        }
        return mrpPrice.subtract(purchasePrice);
    }

    public BigDecimal getPurchaseLinePrice() {
        if (purchasePrice == null || purchaseQty == null) {
            return BigDecimal.ZERO;
        }
        return purchasePrice
                .multiply(BigDecimal.valueOf(purchaseQty));
//                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getActualLinePrice() {
        if (purchasePrice == null || actualQty == null) {
            return BigDecimal.ZERO;
        }
        return purchasePrice
                .multiply(BigDecimal.valueOf(actualQty));
//                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getGiftOrBonusLinePrice() {
        if (purchasePrice == null || giftOrBonusQty == null) {
            return BigDecimal.ZERO;
        }
        return purchasePrice
                .multiply(BigDecimal.valueOf(giftOrBonusQty));
//                .setScale(2, RoundingMode.HALF_UP);
    }
}
