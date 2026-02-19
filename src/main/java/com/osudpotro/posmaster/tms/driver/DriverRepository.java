package com.osudpotro.posmaster.tms.driver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DriverRepository extends JpaSpecificationExecutor<Driver>, JpaRepository<Driver, Long> {
    @Transactional
    @Modifying
    @Query("update Driver c set c.status = :status where c.id in :ids")
    int deleteBulkDriver(@Param("ids") List<Long> ids, @Param("status") Long status);
}
