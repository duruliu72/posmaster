package com.osudpotro.posmaster.purchase.check;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.purchase.PurchaseType;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "final_purchase_requisitions")
public class FinalPurchaseRequisition extends BaseEntity {
    private String requsitionRef;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_requisition_id")
    private PurchaseRequisition purchaseRequisition;
    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_type", nullable = false)
    private PurchaseType purchaseType;
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;
    private BigDecimal overallDiscount;
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    //1 or Null=Not DELIVERED,2=DELIVERED Via Transport
    private Integer transferStatus=1;
    @JsonIgnore
    @OneToMany(mappedBy = "finalPurchaseRequisition", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FinalPurchaseRequisitionItem> items = new ArrayList<>();
    public int getTotalItems() {
        return items.size();
    }
    public int getTotalQty() {
        return items.stream()
                .filter(i ->
                        i.getPurchaseQty() != null
                )
                .mapToInt(FinalPurchaseRequisitionItem::getPurchaseQty)
                .sum();
    }
    public int getTotalGiftOrBonusQty() {
        return items.stream()
                .filter(i ->
                        i.getGiftOrBonusQty() != null
                )
                .mapToInt(FinalPurchaseRequisitionItem::getGiftOrBonusQty)
                .sum();
    }
    public BigDecimal getTotalPrice() {
        return items.stream()
                .filter(i ->
                        i.getPurchaseQty() != null && i.getProductDetail().getPurchasePrice() != null
                )
                .map(i ->
                        i.getProductDetail().getPurchasePrice()
                                .multiply(BigDecimal.valueOf(i.getPurchaseQty()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }
    public BigDecimal getTotalGiftOrBonusPrice() {
        return items.stream()
                .filter(i ->
                        i.getGiftOrBonusQty() != null &&
                                i.getPurchasePrice() != null
                )
                .map(i ->
                        i.getPurchasePrice()
                                .multiply(BigDecimal.valueOf(i.getGiftOrBonusQty()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }
}