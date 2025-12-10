package com.osudpotro.posmaster.resource.ui;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UiResourceActionRepository extends JpaRepository<UiResourceAction, Long> {
    boolean existsByUiResourceIdAndActionId(Long uiResourceId, Long actionId);
    Optional<UiResourceAction> findByUiResourceIdAndActionId(Long uiResourceId, Long actionId);
}
