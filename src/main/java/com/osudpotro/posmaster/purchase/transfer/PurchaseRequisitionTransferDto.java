package com.osudpotro.posmaster.purchase.transfer;

import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.purchase.dto.BranchDto;
import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisition;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseRequisitionTransferDto {
    private Long id;
    private PurchaseRequisition purchaseRequisition;
    private String requsitionRef;
    private String purchaseType;
    private String purchaseKey;
    private OrganizationDto organization;
    private BranchDto branch;
    private BigDecimal overallDiscount;
    private BigDecimal totalPrice;
    private Integer totalQty;
    private BigDecimal totalGiftOrBonusPrice;
    private Integer totalGiftOrBonusQty;
    private String purchaseInvoices;
    private String purchaseInvoiceDocs;
    private String orderRefs;
    private List<PurchaseRequisitionItemTransferDto> items = new ArrayList<>();
    private Integer transferStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
