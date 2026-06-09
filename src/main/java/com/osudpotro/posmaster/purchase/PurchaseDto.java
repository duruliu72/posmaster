package com.osudpotro.posmaster.purchase;

import com.osudpotro.posmaster.branch.BranchDto;
import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.purchase.checked.CheckedPurchaseRequisitionDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionDto;
import com.osudpotro.posmaster.supplier.SupplierDto;
import com.osudpotro.posmaster.user.UserPlainDto;
import com.osudpotro.posmaster.warehouse.WarehouseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseDto {
    private Long id;
    private String purchaseRef;
    private PurchaseRequisitionDto purchaseRequisition;
    private CheckedPurchaseRequisitionDto checkedPurchaseRequisition;
    private String requsitionRef;
    private PurchaseType purchaseType;
    private OrganizationDto organization;
    private BranchDto branch;
    private WarehouseDto warehouse;
    private SupplierDto supplier;
    private String purchaseBatchNo;
    private BigDecimal overallDiscount;
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    private UserPlainDto addedBy;
    private LocalDateTime purchaseAt;
    private UserPlainDto updatedBy;
    private LocalDateTime updatedAt;
    private Integer purchaseStatus;
    private List<PurchaseDetailDto> items = new ArrayList<>();
}
