package com.osudpotro.posmaster.sale;

import com.osudpotro.posmaster.branch.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface SaleRepository extends JpaSpecificationExecutor<Sale>, JpaRepository<Sale, Long> {
    Sale findTopByOrderByCreatedAtDesc();
    Optional<Sale> findByIdAndBranch(Long saleId, Branch branch);
    Page<Sale> findByBranch(Branch branch, Pageable pageable);
}