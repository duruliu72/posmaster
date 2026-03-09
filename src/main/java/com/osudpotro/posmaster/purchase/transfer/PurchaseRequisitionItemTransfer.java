package com.osudpotro.posmaster.purchase.transfer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.product.ProductDetail;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "purchase_requisition_item_transfers", uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "purchase_requisition_transfer_id",
                "purchase_requisition_item_id",
                "product_id",
                "product_detail_id"
        }
))
public class PurchaseRequisitionItemTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_requisition_transfer_id")
    private PurchaseRequisitionTransfer purchaseRequisitionTransfer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_requisition_id")
    private PurchaseRequisition purchaseRequisition;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_requisition_item_id")
    private PurchaseRequisitionItem purchaseRequisitionItem;
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
    private Integer giftOrBonusQty;
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
    public BigDecimal getGiftOrBonusLinePrice() {
        if (purchasePrice == null || giftOrBonusQty == null) {
            return BigDecimal.ZERO;
        }
        return purchasePrice
                .multiply(BigDecimal.valueOf(giftOrBonusQty));
//                .setScale(2, RoundingMode.HALF_UP);
    }
}
