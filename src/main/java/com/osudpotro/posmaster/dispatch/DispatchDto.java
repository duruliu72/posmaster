package com.osudpotro.posmaster.dispatch;

import com.osudpotro.posmaster.branch.BranchDto;
import com.osudpotro.posmaster.organization.OrganizationDto;
import com.osudpotro.posmaster.user.UserPlainDto;
import com.osudpotro.posmaster.warehouse.WarehouseDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DispatchDto {
    private Long id;
    private String dispatchRef;
    private String dispatchInvoice;
    private OrganizationDto organization;
    private BranchDto requestedBranch;
    private BranchDto requestReceivedBranch;
    private WarehouseDto warehouse;
    private UserPlainDto requestedBy;
    private LocalDateTime requestedAt;
    private UserPlainDto requestReceivedBy;
    private LocalDateTime requestReceivedAt;
    private UserPlainDto dispatchBy;
    private LocalDateTime dispatchAt;
    private Integer dispatchStatus;
}
