package com.osudpotro.posmaster.sale;

import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import com.osudpotro.posmaster.product.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    Optional<SaleItem> findBySaleAndPurchaseDetail(Sale sale, PurchaseDetail purchaseDetail);
}