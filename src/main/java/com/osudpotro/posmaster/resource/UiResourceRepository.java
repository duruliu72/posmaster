package com.osudpotro.posmaster.resource;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UiResourceRepository extends JpaRepository<UiResource, Long> {
    Optional<UiResource> findByName(String name);
}
