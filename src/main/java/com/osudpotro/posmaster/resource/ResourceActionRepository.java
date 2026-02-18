package com.osudpotro.posmaster.resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResourceActionRepository extends JpaRepository<ResourceAction, Long> {
    boolean existsByResourceIdAndActionId(Long resourceId, Long actionId);
    Optional<ResourceAction> findByResourceIdAndActionId(Long resourceId, Long actionId);
}
