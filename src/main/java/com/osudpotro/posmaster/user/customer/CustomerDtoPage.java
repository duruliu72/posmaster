package com.osudpotro.posmaster.user.customer;

import lombok.Data;

@Data
public class CustomerDtoPage extends CustomerDto {
    //    Customer
    private long totalAddressElements;
    private int totalAddressPages;
    private int addressPageNumber;
    private int addressPageSize;
    //    Wallet
    private long totalWalletElements;
    private int totalWalletPages;
    private int walletPageNumber;
    private int walletPageSize;
}
