package com.osudpotro.posmaster.sale;

import lombok.Data;

@Data
public class SaleFilter {
    private String saleRef;
    private String customerName;
    private String email;
    private String mobile;
    private Integer saleStatus;
    private Integer paymentStatus;
}