package com.osudpotro.posmaster.newsletter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewsLetterRepository extends JpaSpecificationExecutor<NewsLetter>, JpaRepository<NewsLetter,Long> {
    boolean existsByName(String name);
    @Transactional
    @Modifying
    @Query("update NewsLetter nl set nl.status = :status where nl.id in :ids")
    int deleteBulkNewsLetter(@Param("ids") List<Long> ids, @Param("status") Long status);
}
