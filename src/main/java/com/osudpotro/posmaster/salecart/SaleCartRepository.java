package com.osudpotro.posmaster.salecart;

import com.osudpotro.posmaster.branch.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SaleCartRepository extends JpaSpecificationExecutor<SaleCart>, JpaRepository<SaleCart, Long> {
    Optional<SaleCart> findByIdAndBranch(Long saleCartId,Branch branch);
}
