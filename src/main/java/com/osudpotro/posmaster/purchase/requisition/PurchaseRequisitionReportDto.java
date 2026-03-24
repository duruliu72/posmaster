package com.osudpotro.posmaster.purchase.requisition;

import com.osudpotro.posmaster.branch.BranchDto;
import com.osudpotro.posmaster.organization.OrganizationDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseRequisitionReportDto {
    private Long id;
    private String requsitionRef;
    private String purchaseType;
    private String purchaseKey;
    private OrganizationDto organization;
    private BranchDto branch;
    private BranchDto rootBranch;
    private BranchDto reqBranch;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal overallDiscount;
    private BigDecimal totalPrice;
    private Integer totalQty;
    private BigDecimal totalActualPrice;
    private Integer totalActualQty;
    private BigDecimal totalGiftOrBonusPrice;
    private Integer totalGiftOrBonusQty;
    private Integer requisitionStatus;
    private String note;
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    private Boolean isFinal;
    private List<PurchaseRequisitionItemReportDTO> items = new ArrayList<>();
}
