package com.osudpotro.posmaster.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.purchase.checked.CheckedPurchaseRequisition;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import com.osudpotro.posmaster.supplier.Supplier;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String purchaseRef;//invoice
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_requisition_id")
    private PurchaseRequisition purchaseRequisition;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_purchase_requisition_id")
    private CheckedPurchaseRequisition checkedPurchaseRequisition;
    private String requsitionRef;
    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_type", nullable = false)
    private PurchaseType purchaseType;
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;
    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;
    private String purchaseBatchNo;
    private BigDecimal overallDiscount;
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "added_by", nullable = true)
    private User addedBy;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "updated_by")
    private User updatedBy;
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime purchaseAt;
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    //1 or Null=Assign to Transport,2=Received Via Transport,3=Added to Inventory
    private Integer purchaseStatus = 1;
    @JsonIgnore
    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<PurchaseDetail> items = new ArrayList<>();


    public String getGeneratePurchaseBatchNo() {
        String tripPrefix="BATCH";
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        long nextSeq = 1;
        if (this.getPurchaseRef() != null) {
            String lastTripRef = this.getPurchaseRef();
            String lastPart = lastTripRef.length() > 5 ? lastTripRef.substring(lastTripRef.length() - 6) : lastTripRef;
            if (!lastPart.isEmpty()) {
                try {
                    nextSeq = Long.parseLong(lastPart) + 1;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        //Format String
        return String.format("%s-%s-%06d",tripPrefix, datePart, nextSeq);
    }

    public String getGeneratePurchaseRef() {
        String tripPrefix="PUR";
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        long nextSeq = 1;
        if (this.getPurchaseRef() != null) {
            String lastTripRef = this.getPurchaseRef();
            String lastPart = lastTripRef.length() > 5 ? lastTripRef.substring(lastTripRef.length() - 6) : lastTripRef;
            if (!lastPart.isEmpty()) {
                try {
                    nextSeq = Long.parseLong(lastPart) + 1;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        //Format String
        return String.format("%s-%s-%06d",tripPrefix, datePart, nextSeq);
    }
}
