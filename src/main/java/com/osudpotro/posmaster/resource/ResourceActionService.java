package com.osudpotro.posmaster.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceActionService {
    @Autowired
    private ResourceActionRepository resourceActionRepository;

    boolean existsResourceAction(Long resourceId, Long actionId) {
        return resourceActionRepository.existsByResourceIdAndActionId(resourceId, actionId);
    }

    public ResourceAction getResourceActionEntity(Long resourceId, Long actionId) {
        return resourceActionRepository.findByResourceIdAndActionId(resourceId,actionId).orElse(null);
    }
}
