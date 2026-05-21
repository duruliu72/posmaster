package com.osudpotro.posmaster.sale;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SalePaymentDto {
    private Long id;
    private String paymentMethod;
    private String trxId;
    private BigDecimal creditAmount;
    private BigDecimal debitAmount;
    private String transactionType;
}