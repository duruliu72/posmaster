package com.osudpotro.posmaster.sale;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SalePaymentDto {
    private Long id;
    private String paymentMethod;
    private String trxId;
    private Boolean isSysGenTrx;
    private BigDecimal cashIn;
    private BigDecimal cashOut;
    private Integer transactionType;
}