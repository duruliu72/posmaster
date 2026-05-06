package com.osudpotro.posmaster.user.customer.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AddressRepository extends JpaSpecificationExecutor<Address>, JpaRepository<Address, Long> {
}
