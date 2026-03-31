package com.osudpotro.posmaster.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.product.ProductDetail;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionItem;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "purchase_details", uniqueConstraints = @UniqueConstraint(
        columnNames = {
                "purchase_id",
                "purchase_requisition_item_id",
                "product_id",
                "product_detail_id"
        }
))
public class PurchaseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;
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
    private BigDecimal purchaseDiscount;
    private Integer purchaseQty;
    private Integer giftOrBonusQty;
    private Integer atomQty;
    private String barCode;
    private String productionBatchNo;
    private LocalDateTime manufactureDate;
    private LocalDateTime expiredDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "added_by", nullable = true)
    private User addedBy;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "updated_by")
    private User updatedBy;
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime addedAt;
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
