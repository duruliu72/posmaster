package com.osudpotro.posmaster.requisitiontype;

import com.osudpotro.posmaster.requisition.RequisitionApproverUpdateRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequisitionTypeUpdateRequest {
    private String name;
    private String requisitionTypeKey;
    private List<RequisitionApproverUpdateRequest> approvers =  new ArrayList<>();
}
