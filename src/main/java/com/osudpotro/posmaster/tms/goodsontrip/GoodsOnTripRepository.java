package com.osudpotro.posmaster.tms.goodsontrip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GoodsOnTripRepository extends JpaSpecificationExecutor<GoodsOnTrip>, JpaRepository<GoodsOnTrip, Long> {
    GoodsOnTrip findTopByOrderByCreatedAtDesc();
}
