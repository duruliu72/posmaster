package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.purchase.requisition.PurchaseRequisitionDto;
import com.osudpotro.posmaster.requisitiontype.RequisitionTypeDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class RequisitionDto {
    private Long id;
    private String requsitionRef;
    private PurchaseRequisitionDto purchaseRequisition;
    private RequisitionTypeDto requisitionType;
    private Integer reviewCount;
    private List<RequisitionOnPathDto> requisitionOnPaths = new ArrayList<>();
    private Integer requisitionStatus;
    private LocalDateTime createdAt;
    private String note;
}
