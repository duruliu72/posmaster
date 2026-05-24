package com.osudpotro.posmaster.user.customer.wallet;

import org.springframework.stereotype.Component;

@Component
public class WalletMapper {
    public WalletDto toDto(Wallet wallet) {
        if (wallet == null) {
            return null;
        }
        WalletDto walletDto = new WalletDto();
        walletDto.setId(wallet.getId());
        walletDto.setWalletType(walletDto.getWalletType());
        walletDto.setNote(walletDto.getNote());
        walletDto.setSaleRef(walletDto.getSaleRef());
        walletDto.setCreditAmount(wallet.getCreditAmount());
        walletDto.setDebitAmount(wallet.getDebitAmount());
        return walletDto;
    }
}