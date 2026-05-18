package com.osudpotro.posmaster.sale;

import lombok.Data;

@Data
public class SaleStatusUpdateRequest {
    private Integer saleStatus;
    private String note;
}