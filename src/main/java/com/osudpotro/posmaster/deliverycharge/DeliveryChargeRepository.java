package com.osudpotro.posmaster.deliverycharge;

import com.osudpotro.posmaster.deliverymethod.DeliveryMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface DeliveryChargeRepository extends JpaSpecificationExecutor<DeliveryCharge>,JpaRepository<DeliveryCharge,Long> {
    boolean existsByDeliveryMethodAndIsActive(DeliveryMethod deliveryMethod,Boolean isActive);
    @Transactional
    @Modifying
    @Query("update DeliveryCharge dm set dm.status = :status where dm.id in :ids")
    int deleteBulkEntity(@Param("ids") List<Long> ids, @Param("status") Long status);
}