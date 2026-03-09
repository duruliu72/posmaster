package com.osudpotro.posmaster.purchase.transfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.purchase.PurchaseType;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionItem;
import com.osudpotro.posmaster.tms.goodsontrip.GoodsOnTrip;
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
@Table(name = "purchase_requisition_transfers")
public class PurchaseRequisitionTransfer extends BaseEntity {
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
    private Integer transferStatus = 1;
    //    @OneToOne(fetch = FetchType.LAZY, optional = true)
//    @JoinColumn(
//            name = "goods_on_trip_id",
//            nullable = true,
//            unique = true
//    )
//    private GoodsOnTrip goodsOnTrip;
    @JsonIgnore
    @OneToMany(mappedBy = "purchaseRequisitionTransfer", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GoodsOnTrip> trips = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "purchaseRequisitionTransfer", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PurchaseRequisitionItemTransfer> items = new ArrayList<>();

    public int getTotalItems() {
        return items.size();
    }

    public int getTotalQty() {
        return items.stream()
                .filter(i ->
                        i.getPurchaseQty() != null
                )
                .mapToInt(PurchaseRequisitionItemTransfer::getPurchaseQty)
                .sum();
    }

    public int getTotalGiftOrBonusQty() {
        return items.stream()
                .filter(i ->
                        i.getGiftOrBonusQty() != null
                )
                .mapToInt(PurchaseRequisitionItemTransfer::getGiftOrBonusQty)
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