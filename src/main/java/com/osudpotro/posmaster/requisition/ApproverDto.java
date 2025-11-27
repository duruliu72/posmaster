package com.osudpotro.posmaster.requisition;

import lombok.Data;

@Data
public class ApproverDto {
    private UserDto user ;
    private UserDto nextUser;
}
