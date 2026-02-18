package com.osudpotro.posmaster.resource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends JpaSpecificationExecutor<Resource>, JpaRepository<Resource, Long> {
    boolean existsByName(String name);

    boolean existsByResourceKey(String resourceKey);

    boolean existsByUrl(String url);

    Optional<Resource> findByName(String name);

    @Transactional
    @Modifying
    @Query("update Resource uir set uir.status = :status where uir.id in :ids")
    int deleteBulkResource(@Param("ids") List<Long> ids, @Param("status") Long status);
}
