package com.osudpotro.posmaster.purchase.checked;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osudpotro.posmaster.common.BaseEntity;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import com.osudpotro.posmaster.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "checked_purchase_requisitions")
public class CheckedPurchaseRequisition extends BaseEntity {
    private String requsitionRef;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_requisition_id")
    private PurchaseRequisition purchaseRequisition;
    private BigDecimal overallDiscount;
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "check_by_branch_man")
    private User checkByBranchMan;
    private LocalDateTime checkByBranchAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "check_by_admin")
    private User checkByAdmin;
    private LocalDateTime checkByAdminAt;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "add_by_inventory_man")
    private User addByInventoryMan;
    private LocalDateTime addByInventoryManAt;
    //1=Checked By Branch,2=Checked By Admin,3=Added to Inventory By InventoryMan
    private Integer checkedStatus = 1;
    @JsonIgnore
    @OneToMany(mappedBy = "checkedPurchaseRequisition", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CheckedPurchaseRequisitionItem> items = new ArrayList<>();
    public int getTotalItems() {
        return items.size();
    }
    public int getTotalQty() {
        return items.stream()
                .filter(i ->
                        i.getPurchaseQty() != null
                )
                .mapToInt(CheckedPurchaseRequisitionItem::getPurchaseQty)
                .sum();
    }
    public int getTotalGiftOrBonusQty() {
        return items.stream()
                .filter(i ->
                        i.getGiftOrBonusQty() != null
                )
                .mapToInt(CheckedPurchaseRequisitionItem::getGiftOrBonusQty)
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
