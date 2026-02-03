package com.osudpotro.posmaster.purchase.requisition;

import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.purchase.dto.BranchDto;
import lombok.Data;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double totalPrice;
    private Integer totalQty;
    private Double totalActualPrice;
    private Integer totalActualQty;
    private Double totalGiftPrice;
    private Integer totalGiftQty;
    private Integer requisitionStatus;
    private String note;
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    private Boolean isFinal;
    private List<PurchaseRequisitionItemReportDTO> items = new ArrayList<>();
}
