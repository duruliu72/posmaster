package com.osudpotro.posmaster.purchase.requisition;

import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.purchase.dto.BranchDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
public class PurchaseRequisitionWithItemPageResponse {
    private Long id;
    private String requsitionRef;
    private String purchaseType;
    private String purchaseKey;
    private OrganizationDto organization;
    private BranchDto branch;
    private LocalDateTime createdAt;
    private  LocalDateTime updatedAt;
    //    private Warehouse warehouse;
//    private Supplier supplier;
    private Double totalPrice;
    private Integer totalQty;
    private Integer requisitionStatus;
    private String note;

    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private List<PurchaseRequisitionItemDto> content = new ArrayList<>();
}
