package com.osudpotro.posmaster.requisition;

import lombok.Data;

@Data
public class RequisitionApproverDto {
    private Long id;
    private UserDto prevUser ;
    private UserDto user ;
    private UserDto nextUser;
}
