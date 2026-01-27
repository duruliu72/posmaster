package com.osudpotro.posmaster.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TagRepository extends JpaSpecificationExecutor<Tag>, JpaRepository<Tag,Long> {
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("update Tag o set o.status = :status where o.id in :ids")
    int deleteBulkTag(@Param("ids") List<Long> ids, @Param("status") Long status);
}
