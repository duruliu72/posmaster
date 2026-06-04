package com.osudpotro.posmaster.sale;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalePaymentRepository extends JpaRepository<SalePayment, Long> {
    List<SalePayment> findBySaleId(Long saleId);
}