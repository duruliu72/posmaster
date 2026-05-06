package com.osudpotro.posmaster.deliverymethod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DeliveryMethodRepository extends JpaSpecificationExecutor<DeliveryMethod>,JpaRepository<DeliveryMethod,Long> {
    boolean existsByTitle(String name);
    @Transactional
    @Modifying
    @Query("update DeliveryMethod dm set dm.status = :status where dm.id in :ids")
    int deleteBulkEntity(@Param("ids") List<Long> ids, @Param("status") Long status);
}