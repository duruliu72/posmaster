package com.osudpotro.posmaster.generic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GenericRepository extends JpaSpecificationExecutor<Generic>,JpaRepository<Generic,Long> {
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("update Generic g set g.status = :status where g.id in :ids")
    int deleteBulkGeneric(@Param("ids") List<Long> ids, @Param("status") Long status);
}