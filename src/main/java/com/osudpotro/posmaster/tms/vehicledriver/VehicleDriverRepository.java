package com.osudpotro.posmaster.tms.vehicledriver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface VehicleDriverRepository extends JpaSpecificationExecutor<VehicleDriver>, JpaRepository<VehicleDriver, Long> {
    @Transactional
    @Modifying
    @Query("update VehicleDriver c set c.status = :status where c.id in :ids")
    int deleteBulkVehicleDriver(@Param("ids") List<Long> ids, @Param("status") Long status);
}
