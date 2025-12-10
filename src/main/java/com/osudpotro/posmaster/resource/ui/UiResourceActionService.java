package com.osudpotro.posmaster.resource.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UiResourceActionService {
    @Autowired
    private UiResourceActionRepository uiResourceActionRepository;

    boolean existsUiResourceAction(Long uiResourceId, Long actionId) {
        return uiResourceActionRepository.existsByUiResourceIdAndActionId(uiResourceId, actionId);
    }

    public UiResourceAction getUiResourceActionEntity(Long uiResourceId, Long actionId) {
        return uiResourceActionRepository.findByUiResourceIdAndActionId(uiResourceId,actionId).orElse(null);
    }
}
