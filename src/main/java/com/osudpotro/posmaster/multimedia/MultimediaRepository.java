package com.osudpotro.posmaster.multimedia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MultimediaRepository extends JpaRepository<Multimedia,Long> {
    @Query("select mm from Multimedia mm where mm.id in :ids")
    List<Multimedia> getMultimediaListByIds(@Param("ids") List<Long> ids, @Param("status") Long status);
}
