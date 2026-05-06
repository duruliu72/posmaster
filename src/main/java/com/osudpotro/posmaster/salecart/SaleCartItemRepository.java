package com.osudpotro.posmaster.salecart;

import com.osudpotro.posmaster.purchase.Purchase;
import com.osudpotro.posmaster.purchase.PurchaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.Optional;

public interface SaleCartItemRepository extends JpaSpecificationExecutor<SaleCartItem>, JpaRepository<SaleCartItem, Long> {
    Optional<SaleCartItem> findBySaleCartAndPurchaseAndPurchaseDetail(SaleCart saleCart, Purchase purchase, PurchaseDetail pd);
}
