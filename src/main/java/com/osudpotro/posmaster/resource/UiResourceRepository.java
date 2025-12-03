package com.osudpotro.posmaster.resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UiResourceRepository extends JpaSpecificationExecutor<UiResource>,JpaRepository<UiResource, Long> {
    boolean existsByName(String name);
    boolean existsByUiResourceKey(String uiResourceKey);
    boolean existsByPageUrl(String pageUrl);
    Optional<UiResource> findByName(String name);
    @Transactional
    @Modifying
    @Query("update UiResource uir set uir.status = :status where uir.id in :ids")
    int deleteBulkUiResource(@Param("ids") List<Long> ids, @Param("status") Long status);
}
