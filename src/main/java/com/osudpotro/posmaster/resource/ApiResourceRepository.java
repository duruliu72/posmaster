package com.osudpotro.posmaster.resource;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiResourceRepository extends JpaRepository<ApiResource, Long> {
    boolean existsByName(String name);
    Optional<ApiResource> findByApiUrl(String apiUrl);
}
