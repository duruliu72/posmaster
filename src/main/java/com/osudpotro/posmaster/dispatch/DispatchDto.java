package com.osudpotro.posmaster.dispatch;

import lombok.Data;

@Data
public class DispatchDto {
    private Long id;
    private String requsitionRef;
    private String purchaseType;
}
