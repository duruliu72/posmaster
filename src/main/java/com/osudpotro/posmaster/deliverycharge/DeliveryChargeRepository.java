package com.osudpotro.posmaster.deliverycharge;

import com.osudpotro.posmaster.deliverymethod.DeliveryMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface DeliveryChargeRepository extends JpaSpecificationExecutor<DeliveryCharge>,JpaRepository<DeliveryCharge,Long> {
    boolean existsByDeliveryMethodAndIsActive(DeliveryMethod deliveryMethod,Boolean isActive);
    @Transactional
    @Modifying
    @Query("update DeliveryCharge dm set dm.status = :status where dm.id in :ids")
    int deleteBulkEntity(@Param("ids") List<Long> ids, @Param("status") Long status);



    @Query("SELECT dc FROM DeliveryCharge dc WHERE dc.area.id = :areaId AND dc.deliveryMethod.id = :deliveryMethodId AND dc.isActive = true")
    Optional<DeliveryCharge> findByAreaAndDeliveryMethod(@Param("areaId") Long areaId, @Param("deliveryMethodId") Long deliveryMethodId);




}