package com.osudpotro.posmaster.resource.ui;

import com.osudpotro.posmaster.action.ActionService;
import com.osudpotro.posmaster.auth.AuthService;
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
public class UiResourceService {
    private final AuthService authService;
    private final UiResourceRepository uiResourceRepository;
    private final ActionService actionService;
    private final UiResourceActionService uiResourceActionService;
    private final UiResourceMapper uiResourceMapper;
    private final CsvReader csvReader;

    public List<UiResourceDto> gerAllUiResources() {
        return uiResourceRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(uiResourceMapper::toDto)
                .toList();
    }

    public Page<UiResourceDto> getUiResources(UiResourceFilter filter, Pageable pageable) {
        return uiResourceRepository.findAll(UiResourceSpecification.filter(filter), pageable).map(uiResourceMapper::toDto);
    }

    public int importUiResources(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<UiResource> uiResources = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            if (name == null || name.trim().isEmpty()) {
                continue; // Skip invalid rows
            }
            UiResource uiResource = new UiResource();
            uiResource.setName(name.trim());
            uiResource.setCreatedBy(user);
            uiResources.add(uiResource);
            count++;
        }
        uiResourceRepository.saveAll(uiResources);
        return count;
    }

    public UiResourceDto CreateUiResource(UiResourceCreateRequest request) {
        if (uiResourceRepository.existsByName(request.getName())) {
            throw new DuplicateUiResourceException();
        }
        var user = authService.getCurrentUser();
        var uiResource = uiResourceMapper.toEntity(request);
        if (request.getParentId() != null) {
            var parentUiResource = uiResourceRepository.findById(request.getParentId()).orElseThrow(UiResourceNotFoundException::new);
            uiResource.setParentUiResource(parentUiResource);
        }
        //For UiResourceAction
        List<UiResourceAction> uiResourceActions = new ArrayList<>();
        if (request.getActionWithChecks() != null) {
            for (UiResourceActionRequest item : request.getActionWithChecks()) {
                if (uiResourceActionService.existsUiResourceAction(uiResource.getId(), item.getActionId())) {
                    throw new DuplicateUiResourceActionException();
                }
                UiResourceAction uiResourceAction = new UiResourceAction();
                var action = actionService.getActionEntity(item.getActionId());
                uiResourceAction.setUiResource(uiResource);
                uiResourceAction.setAction(action);
                uiResourceAction.setChecked(item.getChecked());
                uiResourceAction.setCreatedBy(user);
                uiResourceActions.add(uiResourceAction);
            }
        }
        uiResource.setUiResourceActions(uiResourceActions);

        uiResource.setCreatedBy(user);
        uiResourceRepository.save(uiResource);
        return uiResourceMapper.toDto(uiResource);
    }

    public UiResourceDto updateUiResource(Long uiResourceId, UiResourceUpdateRequest request) {
        var uiResource = uiResourceRepository.findById(uiResourceId).orElseThrow(UiResourceNotFoundException::new);
        if (!Objects.equals(uiResource.getName(), request.getName())) {
            if (uiResourceRepository.existsByName(request.getName())) {
                throw new DuplicateUiResourceException();
            }
        }
        var user = authService.getCurrentUser();
        uiResourceMapper.update(request, uiResource);

        if (request.getParentId() != null) {
            if (!uiResourceId.equals(request.getParentId())) {
                var parentUiResource = uiResourceRepository.findById(request.getParentId()).orElseThrow(UiResourceNotFoundException::new);
                uiResource.setParentUiResource(parentUiResource);
            } else {
                throw new UiResourceSelfParentException();
            }
        } else {
            uiResource.setParentUiResource(null);
        }

        //For UiResourceAction
        List<UiResourceAction> uiResourceActions = new ArrayList<>();
        if (request.getActionWithChecks() != null) {
            for (UiResourceActionRequest item : request.getActionWithChecks()) {
//                Fetch By uiResourceId and actionId
                UiResourceAction uiResourceAction = uiResourceActionService.getUiResourceActionEntity(uiResource.getId(), item.getActionId());
                if (uiResourceAction != null) {
                    var action = actionService.getActionEntity(item.getActionId());
                    uiResourceAction.setAction(action);
                    uiResourceAction.setChecked(item.getChecked());
                    uiResourceAction.setUpdatedBy(user);
                    uiResourceActions.add(uiResourceAction);
                } else {
                    if (uiResourceActionService.existsUiResourceAction(uiResource.getId(), item.getActionId())) {
                        throw new DuplicateUiResourceActionException();
                    }
                    UiResourceAction newUiResourceAction = new UiResourceAction();
                    var action = actionService.getActionEntity(item.getActionId());
                    newUiResourceAction.setUiResource(uiResource);
                    newUiResourceAction.setAction(action);
                    newUiResourceAction.setChecked(item.getChecked());
                    newUiResourceAction.setCreatedBy(user);
                    uiResourceActions.add(newUiResourceAction);
                }
            }
        }
        uiResource.getUiResourceActions().clear();
        uiResource.getUiResourceActions().addAll(uiResourceActions);
        uiResource.setUpdatedBy(user);
        uiResourceRepository.save(uiResource);
        return uiResourceMapper.toDto(uiResource);
    }

    public UiResourceDto getUiResource(Long uiResourceId) {
        var uiResource = uiResourceRepository.findById(uiResourceId).orElseThrow(UiResourceNotFoundException::new);
        if (uiResource.getParentUiResource() != null) {
            var uiResourceParent = uiResourceRepository.findById(uiResource.getParentUiResource().getId()).orElseThrow(UiResourceNotFoundException::new);
            uiResource.setParentUiResource(uiResourceParent);
        }
        return uiResourceMapper.toDto(uiResource);
    }

    public UiResourceDto getUiResourceOrNull(Long uiResourceId) {
        var uiResource = uiResourceRepository.findById(uiResourceId).orElseThrow();
        return uiResourceMapper.toDto(uiResource);
    }

    public UiResource getUiResourceEntity(Long uiResourceId) {
        return uiResourceRepository.findById(uiResourceId).orElseThrow(UiResourceNotFoundException::new);
    }

    public UiResourceDto activeUiResource(Long uiResourceId) {
        var uiResource = uiResourceRepository.findById(uiResourceId).orElseThrow(() -> new UiResourceNotFoundException("UiResource not found with ID: " + uiResourceId));
        var user = authService.getCurrentUser();
        uiResource.setStatus(1);
        uiResource.setUpdatedBy(user);
        uiResourceRepository.save(uiResource);
        return uiResourceMapper.toDto(uiResource);
    }

    public UiResourceDto deactivateUiResource(Long uiResourceId) {
        var uiResource = uiResourceRepository.findById(uiResourceId).orElseThrow(() -> new UiResourceNotFoundException("UiResource not found with ID: " + uiResourceId));
        var user = authService.getCurrentUser();
        uiResource.setStatus(2);
        uiResource.setUpdatedBy(user);
        uiResourceRepository.save(uiResource);
        return uiResourceMapper.toDto(uiResource);
    }

    public UiResourceDto deleteUiResource(Long uiResourceId) {
        var uiResource = uiResourceRepository.findById(uiResourceId).orElseThrow(() -> new UiResourceNotFoundException("UiResource not found with ID: " + uiResourceId));
        var user = authService.getCurrentUser();
        uiResource.setStatus(3);
        uiResource.setUpdatedBy(user);
        uiResourceRepository.save(uiResource);
        return uiResourceMapper.toDto(uiResource);
    }

    public int deleteBulkUiResource(List<Long> ids) {
        return uiResourceRepository.deleteBulkUiResource(ids, 3L);
    }
}
