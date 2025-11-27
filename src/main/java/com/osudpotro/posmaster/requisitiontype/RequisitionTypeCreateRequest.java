package com.osudpotro.posmaster.requisitiontype;


import com.osudpotro.posmaster.requisition.ApproverCreateRequest;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RequisitionTypeCreateRequest {
    private String name;
    private String requisitionTypeKey;
    private Set<ApproverCreateRequest> approvers =  new HashSet<>();
}
