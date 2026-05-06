package com.osudpotro.posmaster.dispatch;

import com.osudpotro.posmaster.branch.BranchDto;
import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.user.UserPlainDto;
import com.osudpotro.posmaster.warehouse.WarehouseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class DispatchDto {
    private Long id;
    private String dispatchRef;
    private String dispatchInvoice;
    private OrganizationDto organization;
    private BranchDto requesterBranch;
    private BranchDto acceptorBranch;
    private WarehouseDto warehouse;
    private UserPlainDto sendByRequester;
    private LocalDateTime sendAtByRequester;
    private String sendNoteByRequester;
    private UserPlainDto acceptByRequester;
    private LocalDateTime acceptAtByRequester;
    private String acceptNoteByRequester;
    private UserPlainDto acceptByAcceptor;
    private LocalDateTime acceptAtByAcceptor;
    private String acceptNoteByAcceptor;
    private UserPlainDto sendByAcceptor;
    private LocalDateTime sendAtByAcceptor;
    private String sendNoteByAcceptor;
    private Integer dispatchStatus;
    private LocalDateTime createdAt;
    private Integer totalDispatchQty;
    private BigDecimal totalPurchasePrice;
    private List<DispatchItemDto> items = new ArrayList<>();
}
