package com.osudpotro.posmaster.user.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaSpecificationExecutor<Customer>, JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByMobile(String mobile);
    boolean existsByEmail(String email);
    boolean existsByMobile(String mobile);
    boolean existsByEmailOrMobile(String email, String mobile);
    Optional<Customer> findByOtpCode(String otpCode);

    @Transactional
    @Modifying
    @Query("update Customer c set c.status = :status where c.id in :ids")
    int deleteBulkCustomer(@Param("ids") List<Long> ids, @Param("status") Long status);

    @Query(value = "SELECT * FROM customers WHERE LOWER(email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(mobile) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
    List<Customer> searchByEmailOrMobile(@Param("keyword") String keyword);


    }






