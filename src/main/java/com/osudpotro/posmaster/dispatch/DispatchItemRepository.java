package com.osudpotro.posmaster.dispatch;

import com.osudpotro.posmaster.product.Product;
import com.osudpotro.posmaster.product.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DispatchItemRepository extends JpaSpecificationExecutor<DispatchItem>, JpaRepository<DispatchItem, Long> {
    Optional<DispatchItem> findByDispatchAndProductAndProductDetail(Dispatch dispatch, Product product, ProductDetail productDetail);
}
