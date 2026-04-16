package com.osudpotro.posmaster.address.upozila;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UpozilaRepository extends JpaSpecificationExecutor<Upozila>,JpaRepository<Upozila,Long> {
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("update Upozila b set b.status = :status where b.id in :ids")
    int deleteBulkEntity(@Param("ids") List<Long> ids, @Param("status") Long status);
}