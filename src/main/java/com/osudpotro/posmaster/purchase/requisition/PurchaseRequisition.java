package com.osudpotro.posmaster.purchase.requisition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.purchase.PurchaseType;
import com.osudpotro.posmaster.requisition.Requisition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "purchase_requisitions")
public class PurchaseRequisition extends BaseEntity {
    private String requsitionRef;
    //c.p=Company Purchase,l.p=Locale Purchase,p.o=purchase order ,procurement
    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_type", nullable = false)
    private PurchaseType purchaseType;
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    private Boolean isFinal = false;
    //    @ManyToOne(fetch = FetchType.LAZY)
//    private Warehouse warehouse;
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Supplier supplier;
    @JsonIgnore
    @OneToMany(mappedBy = "purchaseRequisition", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PurchaseRequisitionItem> items = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(
            name = "requisition_id",
            nullable = true,
            unique = true
    )
    private Requisition requisition;

    public int getTotalItems() {
        return items.size();
    }

    public int getTotalQty() {
        return items.stream()
                .filter(i ->
                        i.getPurchaseQty() != null
                )
                .mapToInt(PurchaseRequisitionItem::getPurchaseQty)
                .sum();
    }

    public int getTotalActualQty() {
        return items.stream()
                .filter(i ->
                        i.getActualQty() != null
                )
                .mapToInt(PurchaseRequisitionItem::getActualQty)
                .sum();
    }

    public int getTotalGiftQty() {
        return items.stream()
                .filter(i ->
                        i.getGiftQty() != null
                )
                .mapToInt(PurchaseRequisitionItem::getGiftQty)
                .sum();
    }

    public Double getTotalPrice() {
        return items.stream()
                .filter(i ->
                        i.getPurchaseQty() != null && i.getProductDetail().getPurchasePrice() != null
                )
                .mapToDouble(i ->
                        i.getPurchaseQty()
                                * i.getProductDetail().getPurchasePrice()
                )
                .sum();
    }

    public Double getTotalActualPrice() {
        return items.stream()
                .filter(i ->
                        i.getActualQty() != null &&
                                i.getPurchasePrice() != null
                )
                .mapToDouble(i ->
                        i.getActualQty()
                                * i.getPurchasePrice()
                )
                .sum();
    }

    public Double getTotalGiftPrice() {
        return items.stream()
                .filter(i ->
                        i.getGiftQty() != null &&
                                i.getPurchasePrice() != null
                )
                .mapToDouble(i ->
                        i.getGiftQty()
                                * i.getPurchasePrice()
                )
                .sum();
    }
    //    Like draft=1, Submitted=2,3=Approved,4=Rejected,5=Cancelled,6=Closed(After all finally process done)
//    private Integer requisitionStatus = 1;
//    private String note;
}


//    like draft=1, Submitted=2, Pending Approval=3,4=Approved,5=Rejected,6=Cancelled,7=PO Created,8=Completed,9=Closed