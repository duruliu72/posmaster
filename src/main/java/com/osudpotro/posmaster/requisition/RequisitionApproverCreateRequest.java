package com.osudpotro.posmaster.requisition;

import lombok.Data;

@Data
public class RequisitionApproverCreateRequest {
    private Long userId ;
    private Long nextUserId;
    private Long requisitionTypeId;
}
