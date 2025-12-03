package com.osudpotro.posmaster.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerRepository extends JpaSpecificationExecutor<Customer>, JpaRepository<Customer, Long> {
    boolean existsByEmailOrPhone(String email, String mobile);
    boolean existsByEmail(String email);
    boolean existsByPhone(String mobile);
    @Transactional
    @Modifying
    @Query("update Customer c set c.status = :status where c.id in :ids")
    int deleteBulkCustomer(@Param("ids") List<Long> ids, @Param("status") Long status);
}
