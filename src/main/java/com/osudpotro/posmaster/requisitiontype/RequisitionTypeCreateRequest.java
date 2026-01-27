package com.osudpotro.posmaster.requisitiontype;


import com.osudpotro.posmaster.requisition.RequisitionApproverCreateRequest;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RequisitionTypeCreateRequest {
    private String name;
    private String requisitionTypeKey;
    private Set<RequisitionApproverCreateRequest> approvers =  new HashSet<>();
}
