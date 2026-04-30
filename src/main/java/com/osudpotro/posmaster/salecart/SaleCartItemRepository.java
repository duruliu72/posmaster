package com.osudpotro.posmaster.salecart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SaleCartItemRepository extends JpaSpecificationExecutor<SaleCartItem>, JpaRepository<SaleCartItem, Long> {
}
