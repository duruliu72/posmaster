package com.osudpotro.posmaster.user.wallet;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface WalletRepository extends JpaSpecificationExecutor<Wallet>, JpaRepository<Wallet, Long> {

}
