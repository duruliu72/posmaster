package com.osudpotro.posmaster.variantunit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VariantUnitRepository extends JpaSpecificationExecutor<VariantUnit>,JpaRepository<VariantUnit,Long> {
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("update VariantUnit vu set vu.status = :status where vu.id in :ids")
    int deleteBulkVariantUnit(@Param("ids") List<Long> ids, @Param("status") Long status);
}