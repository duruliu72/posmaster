package com.osudpotro.posmaster.picture;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture,Long> {
    boolean existsByName(String name);
}
