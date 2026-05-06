package com.osudpotro.posmaster.offerhub.offer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OfferRepository extends JpaSpecificationExecutor<Offer>,JpaRepository<Offer,Long> {
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("update Offer dm set dm.status = :status where dm.id in :ids")
    int deleteBulkEntity(@Param("ids") List<Long> ids, @Param("status") Long status);
}