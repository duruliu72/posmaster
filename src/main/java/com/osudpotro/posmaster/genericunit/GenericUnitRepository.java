package com.osudpotro.posmaster.genericunit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GenericUnitRepository extends JpaSpecificationExecutor<GenericUnit>,JpaRepository<GenericUnit,Long> {
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("update GenericUnit gu set gu.status = :status where gu.id in :ids")
    int deleteBulkGenericUnit(@Param("ids") List<Long> ids, @Param("status") Long status);
}