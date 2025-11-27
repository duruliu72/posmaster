package com.osudpotro.posmaster.requisitiontype;

import com.osudpotro.posmaster.requisition.ApproverDto;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RequisitionTypeDto {
    private Long id;
    private String name;
    private String requisitionTypeKey;
    private Set<ApproverDto> approvers = new HashSet<>();

}
