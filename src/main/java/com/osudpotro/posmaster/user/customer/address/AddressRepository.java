package com.osudpotro.posmaster.user.customer.address;

import com.osudpotro.posmaster.user.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaSpecificationExecutor<Address>, JpaRepository<Address, Long> {
    List<Address> findAllByOrderByIdAsc();
    Optional<Address> findByCustomerAndIsDefault(Customer customer, Boolean isDefault);
}
