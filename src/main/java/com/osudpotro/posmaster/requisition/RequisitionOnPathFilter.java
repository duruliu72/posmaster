package com.osudpotro.posmaster.requisition;

import lombok.Data;

@Data
public class RequisitionOnPathFilter {
    private String requsitionRef;
    private Integer requisitionType;
    private Integer requisitionStatus;
    private Integer approvedStatus;
}
