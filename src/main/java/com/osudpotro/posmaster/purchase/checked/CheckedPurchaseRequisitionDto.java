package com.osudpotro.posmaster.purchase.checked;

import com.osudpotro.posmaster.branch.BranchDto;
import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.product.ProductDetailDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionDto;
import com.osudpotro.posmaster.user.UserPlainDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CheckedPurchaseRequisitionDto {
    private Long id;
    private String requsitionRef;
    private BigDecimal overallDiscount;
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    private UserPlainDto checkByBranchMan;
    private LocalDateTime checkByBranchAt;
    private UserPlainDto checkByAdmin;
    private LocalDateTime checkByAdminAt;
    private UserPlainDto addByInventoryMan;
    private LocalDateTime addByInventoryManAt;
    private Integer checkedStatus = 1;
    //Purchase Requisition
    private PurchaseRequisitionDto purchaseRequisition;
    private Long purchaseRequisitionId;
    private String purchaseType;
    private String purchaseKey;
    private OrganizationDto organization;
    private BranchDto branch;
    private BigDecimal totalPrice;
    private Integer totalQty;
    private BigDecimal totalGiftOrBonusPrice;
    private Integer totalGiftOrBonusQty;
    private ProductDetailDto purchaseProductUnit;
    private List<CheckedPurchaseRequisitionItemDto> items = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    //  Requsition Field
    private Long requisitionId;
    private Integer requisitionStatus;
}
