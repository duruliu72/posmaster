package com.osudpotro.posmaster.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaSpecificationExecutor<Category>, JpaRepository<Category, Long> {
    boolean existsByName(String name);

    Optional<Category> findByPictureId(Long pictureId);
    @Query("select c from Category c where c.parentCat.id = :parentId")
    List<Category> findByParentId(Long parentId);
    @Transactional
    @Modifying
    @Query("update Category cat set cat.status = :status where cat.id in :ids")
    int deleteBulkCategory(@Param("ids") List<Long> ids, @Param("status") Long status);
}