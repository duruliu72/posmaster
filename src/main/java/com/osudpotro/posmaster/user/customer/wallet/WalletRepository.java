package com.osudpotro.posmaster.user.customer.wallet;


import com.osudpotro.posmaster.user.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface WalletRepository extends JpaSpecificationExecutor<Wallet>, JpaRepository<Wallet, Long> {
    List<Wallet> findAllByCustomer(Customer customer);
    Page<Wallet> findAllByCustomer(Customer customer, Pageable pageable);
    Page<Wallet> findAllByCustomer(Customer customer, Specification<Wallet> spec, Pageable pageable);
}
