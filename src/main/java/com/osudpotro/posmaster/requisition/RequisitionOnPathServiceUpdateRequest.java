package com.osudpotro.posmaster.requisition;

import lombok.Data;

@Data
public class RequisitionOnPathServiceUpdateRequest {
    private String comment;
    private Integer approvedStatus;
}
