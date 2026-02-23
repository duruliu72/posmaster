package com.osudpotro.posmaster.tms.vehicletrip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VehicleTripRepository extends JpaSpecificationExecutor<VehicleTrip>, JpaRepository<VehicleTrip, Long> {
    boolean existsByTripRef(String tripRef);
    @Transactional
    @Modifying
    @Query("update VehicleTrip c set c.status = :status where c.id in :ids")
    int deleteBulkVehicleTrip(@Param("ids") List<Long> ids, @Param("status") Long status);
    VehicleTrip findTopByOrderByCreatedAtDesc();
}
