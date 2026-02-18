package com.osudpotro.posmaster.resource;

import com.osudpotro.posmaster.action.ActionService;
import com.osudpotro.posmaster.user.auth.AuthService;
import com.osudpotro.posmaster.utility.CsvReader;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class ResourceService {
    private final AuthService authService;
    private final ResourceRepository resourceRepository;
    private final ActionService actionService;
    private final ResourceActionService resourceActionService;
    private final ResourceMapper resourceMapper;
    private final CsvReader csvReader;

    public List<ResourceDto> gerAllResources() {
        return resourceRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(resourceMapper::toDto)
                .toList();
    }

    public Page<ResourceDto> filterResources(ResourceFilter filter, Pageable pageable) {
        return resourceRepository.findAll(ResourceSpecification.filter(filter), pageable).map(resourceMapper::toDto);
    }

    public int importResources(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<Resource> resources = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            if (name == null || name.trim().isEmpty()) {
                continue; // Skip invalid rows
            }
            Resource resource = new Resource();
            resource.setName(name.trim());
            resource.setCreatedBy(user);
            resources.add(resource);
            count++;
        }
        resourceRepository.saveAll(resources);
        return count;
    }

    public ResourceDto createResource(ResourceCreateRequest request) {
        if (resourceRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException();
        }
        var user = authService.getCurrentUser();
        var resource = resourceMapper.toEntity(request);
        if (request.getParentId() != null) {
            var parentResource = resourceRepository.findById(request.getParentId()).orElseThrow(ResourceNotFoundException::new);
            resource.setParentResource(parentResource);
        }
        //For ResourceAction
        List<ResourceAction> resourceActions = new ArrayList<>();
        if (request.getActionWithChecks() != null) {
            for (ResourceActionRequest item : request.getActionWithChecks()) {
                if (resourceActionService.existsResourceAction(resource.getId(), item.getActionId())) {
                    throw new DuplicateResourceActionException();
                }
                ResourceAction resourceAction = new ResourceAction();
                var action = actionService.getActionEntity(item.getActionId());
                resourceAction.setResource(resource);
                resourceAction.setAction(action);
                resourceAction.setChecked(item.getChecked());
                resourceAction.setCreatedBy(user);
                resourceActions.add(resourceAction);
            }
        }
        resource.setResourceActions(resourceActions);

        resource.setCreatedBy(user);
        resourceRepository.save(resource);
        return resourceMapper.toDto(resource);
    }

    public ResourceDto updateResource(Long resourceId, ResourceUpdateRequest request) {
        var resource = resourceRepository.findById(resourceId).orElseThrow(ResourceNotFoundException::new);
        if (!Objects.equals(resource.getName(), request.getName())) {
            if (resourceRepository.existsByName(request.getName())) {
                throw new DuplicateResourceException();
            }
        }
        var user = authService.getCurrentUser();
        resourceMapper.update(request, resource);

        if (request.getParentId() != null) {
            if (!resourceId.equals(request.getParentId())) {
                var parentResource = resourceRepository.findById(request.getParentId()).orElseThrow(ResourceNotFoundException::new);
                resource.setParentResource(parentResource);
            } else {
                throw new ResourceSelfParentException();
            }
        } else {
            resource.setParentResource(null);
        }

        //For ResourceAction
        List<ResourceAction> resourceActions = new ArrayList<>();
        if (request.getActionWithChecks() != null) {
            for (ResourceActionRequest item : request.getActionWithChecks()) {
//                Fetch By resourceId and actionId
                ResourceAction resourceAction = resourceActionService.getResourceActionEntity(resource.getId(), item.getActionId());
                if (resourceAction != null) {
                    var action = actionService.getActionEntity(item.getActionId());
                    resourceAction.setAction(action);
                    resourceAction.setChecked(item.getChecked());
                    resourceAction.setUpdatedBy(user);
                    resourceActions.add(resourceAction);
                } else {
                    if (resourceActionService.existsResourceAction(resource.getId(), item.getActionId())) {
                        throw new DuplicateResourceActionException();
                    }
                    ResourceAction newResourceAction = new ResourceAction();
                    var action = actionService.getActionEntity(item.getActionId());
                    newResourceAction.setResource(resource);
                    newResourceAction.setAction(action);
                    newResourceAction.setChecked(item.getChecked());
                    newResourceAction.setCreatedBy(user);
                    resourceActions.add(newResourceAction);
                }
            }
        }
        resource.getResourceActions().clear();
        resource.getResourceActions().addAll(resourceActions);
        resource.setUpdatedBy(user);
        resourceRepository.save(resource);
        return resourceMapper.toDto(resource);
    }

    public ResourceDto getResource(Long resourceId) {
        var resource = resourceRepository.findById(resourceId).orElseThrow(ResourceNotFoundException::new);
        if (resource.getParentResource() != null) {
            var resourceParent = resourceRepository.findById(resource.getParentResource().getId()).orElseThrow(ResourceNotFoundException::new);
            resource.setParentResource(resourceParent);
        }
        return resourceMapper.toDto(resource);
    }

    public ResourceDto getResourceOrNull(Long resourceId) {
        var resource = resourceRepository.findById(resourceId).orElseThrow();
        return resourceMapper.toDto(resource);
    }

    public Resource getResourceEntity(Long resourceId) {
        return resourceRepository.findById(resourceId).orElseThrow(ResourceNotFoundException::new);
    }

    public ResourceDto activeResource(Long resourceId) {
        var resource = resourceRepository.findById(resourceId).orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        var user = authService.getCurrentUser();
        resource.setStatus(1);
        resource.setUpdatedBy(user);
        resourceRepository.save(resource);
        return resourceMapper.toDto(resource);
    }

    public ResourceDto deactivateResource(Long resourceId) {
        var resource = resourceRepository.findById(resourceId).orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        var user = authService.getCurrentUser();
        resource.setStatus(2);
        resource.setUpdatedBy(user);
        resourceRepository.save(resource);
        return resourceMapper.toDto(resource);
    }

    public ResourceDto deleteResource(Long resourceId) {
        var resource = resourceRepository.findById(resourceId).orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        var user = authService.getCurrentUser();
        resource.setStatus(3);
        resource.setUpdatedBy(user);
        resourceRepository.save(resource);
        return resourceMapper.toDto(resource);
    }

    public int deleteBulkResource(List<Long> ids) {
        return resourceRepository.deleteBulkResource(ids, 3L);
    }
}
