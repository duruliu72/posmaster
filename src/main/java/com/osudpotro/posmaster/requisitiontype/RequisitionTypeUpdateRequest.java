package com.osudpotro.posmaster.requisitiontype;

import com.osudpotro.posmaster.requisition.ApproverUpdateRequest;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RequisitionTypeUpdateRequest {
    private String name;
    private String requisitionTypeKey;
    private Set<ApproverUpdateRequest> approvers =  new HashSet<>();
}
