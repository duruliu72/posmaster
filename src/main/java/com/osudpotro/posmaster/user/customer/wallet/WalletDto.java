package com.osudpotro.posmaster.user.customer.wallet;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletDto {
    private Long id;
    private Integer walletType;
    private String note;
    private String saleRef;
    private BigDecimal creditAmount;
    private BigDecimal debitAmount;
}
