package com.osudpotro.posmaster.purchase.requisition;

import com.osudpotro.posmaster.branch.BranchDto;
import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.product.ProductDetailDto;
import com.osudpotro.posmaster.purchase.transfer.PurchaseRequisitionTransferDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseRequisitionDto {
    private Long id;
    private String requsitionRef;
    private String purchaseType;
    private String purchaseKey;
    private OrganizationDto organization;
    private BranchDto branch;
    private BranchDto rootBranch;
    private BranchDto reqBranch;
    private BigDecimal overallDiscount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal totalPrice;
    private Integer totalQty;
    private BigDecimal totalActualPrice;
    private Integer totalActualQty;
    private BigDecimal totalGiftOrBonusPrice;
    private Integer totalGiftOrBonusQty;
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    private Boolean isFinal;
    private ProductDetailDto purchaseProductUnit;
    private List<PurchaseRequisitionItemDto> items = new ArrayList<>();
    private List<PurchaseRequisitionTransferDto> prTransferList= new ArrayList<>();
//  Requsition Field
    private Long requisitionId;
    private Integer requisitionStatus;
    private String note;
}
