package com.osudpotro.posmaster.sale;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalePaymentRequest {
    private String paymentMethod;
    private String trxId;
    private BigDecimal paymentAmount;
    private Boolean isSysGenTrx;
}
