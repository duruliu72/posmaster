package com.osudpotro.posmaster.requisitiontype;

import com.osudpotro.posmaster.requisition.RequisitionApproverDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequisitionTypeDto {
    private Long id;
    private String name;
    private String requisitionTypeKey;
    private List<RequisitionApproverDto> approvers = new ArrayList<>();

}
