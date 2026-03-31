package com.osudpotro.posmaster.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.osudpotro.posmaster.branch.Branch;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import com.osudpotro.posmaster.purchase.transfer.PurchaseRequisitionTransfer;
import com.osudpotro.posmaster.supplier.Supplier;
import com.osudpotro.posmaster.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String purchaseRef;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_requisition_transfer_id")
    private PurchaseRequisitionTransfer purchaseRequisitionTransfer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_requisition_id")
    private PurchaseRequisition purchaseRequisition;
    private String requsitionRef;
    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_type", nullable = false)
    private PurchaseType purchaseType;
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch branch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch senderBranch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Branch receiverBranch;
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;
    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;
    private String purchaseBatchNo;
    private BigDecimal overallDiscount;
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime purchaseAt;
    //1 or Null=Assign to Transport,2=Received Via Transport,3=Added to Inventory
    private Integer purchaseStatus = 1;
}
