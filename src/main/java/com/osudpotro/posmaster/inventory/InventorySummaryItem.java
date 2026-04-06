package com.osudpotro.posmaster.inventory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.product.ProductDetail;
import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
//@Entity
//@Table(name = "inventory_summary_items", uniqueConstraints = @UniqueConstraint(
//        columnNames = {
//                "inventory_summary_id",
//                "purchase_id",
//                "purchase_details_id",
//                "product_id",
//                "product_detail_id"
//        }
//))
public class InventorySummaryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "inventory_summary_id")
    private InventorySummary inventorySummary;
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
    private String purchaseBatchNo;
    private String productionBatchNo;
    private LocalDateTime manufactureDate;
    private LocalDateTime expiredDate;
    private Integer stockIn;
    private Integer stockOut;
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
