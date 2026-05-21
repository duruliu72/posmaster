package com.osudpotro.posmaster.user.customer.wallet;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface WalletRepository extends JpaSpecificationExecutor<Wallet>, JpaRepository<Wallet, Long> {

}
