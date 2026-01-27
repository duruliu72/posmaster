package com.osudpotro.posmaster.requisition;

import lombok.Data;

@Data
public class RequisitionOnPathDto {
    private Long id;
    private RequisitionDto requisition;
    private Integer reviewCount;
    private UserDto prevUser ;
    private UserDto user ;
    private UserDto nextUser;
    private Integer approvedStatus;
    private String comment;
}
