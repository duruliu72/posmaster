package com.osudpotro.posmaster.address.area;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AreaRepository extends JpaSpecificationExecutor<Area>,JpaRepository<Area,Long> {

    List<Area> findByStatusAndIsSubArea(Integer status, Boolean isSubArea);
    List<Area> findByParentAreaIdAndStatus(Long parentAreaId, Integer status);
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("update Area b set b.status = :status where b.id in :ids")
    int deleteBulkEntity(@Param("ids") List<Long> ids, @Param("status") Long status);
}