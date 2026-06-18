package com.osudpotro.posmaster.securityadmistration.module;

import com.osudpotro.posmaster.common.DuplicateEntityException;
import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleInfoService {
    @Autowired
    private AuthService authService;
    @Autowired
    private ModuleInfoRepository moduleInfoRepo;
    @Autowired
    private ModuleInfoMapper moduleInfoMapper;

    public List<ModuleInfoDto> getAllEntities() {
        return moduleInfoRepo.findAll()
                .stream()
                .map(moduleInfoMapper::toDto)
                .toList();
    }

    public Page<ModuleInfoDto> getAllEntities(ModuleFilter filter, Pageable pageable) {
        return moduleInfoRepo.findAll(ModuleInfoSpecification.filter(filter), pageable).map(moduleInfoMapper::toDto);
    }

    public ModuleInfoDto getEntity(Long entityId) {
        var entity = moduleInfoRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Module not found with ID: " + entityId));
        return moduleInfoMapper.toDto(entity);
    }

    public ModuleInfoDto createEntity(ModuleCreateRequest request) {
        if (moduleInfoRepo.existsByModuleName(request.getModuleName())) {
            throw new DuplicateEntityException();
        }
        var authUser = authService.getCurrentUser();
        ModuleInfo moduleinfo = new ModuleInfo();
        moduleinfo.setModuleName(request.getModuleName());
        moduleinfo.setModuleCode(request.getModuleCode());
        moduleinfo.setDescription(request.getDescription());
        moduleinfo.setCreatedBy(authUser);
        moduleInfoRepo.save(moduleinfo);
        return moduleInfoMapper.toDto(moduleinfo);
    }

    public ModuleInfoDto updateEntity(Long entityId, ModuleUpdateRequest request) {
        ModuleInfo moduleinfo = moduleInfoRepo.findById(entityId).orElseThrow(EntityNotFoundException::new);
        if (!moduleinfo.getModuleName().equals(request.getModuleName()) && moduleInfoRepo.existsByModuleName(request.getModuleName())) {
            throw new DuplicateEntityException();
        }
        var authUser = authService.getCurrentUser();
        moduleinfo.setModuleName(request.getModuleName());
        moduleinfo.setModuleCode(request.getModuleCode());
        moduleinfo.setDescription(request.getDescription());
        moduleinfo.setUpdatedBy(authUser);
        moduleInfoRepo.save(moduleinfo);
        return moduleInfoMapper.toDto(moduleinfo);
    }

    public ModuleInfoDto deleteEntity(Long entityId) {
        ModuleInfo moduleinfo = moduleInfoRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Area not found with ID: " + entityId));
        var authUser = authService.getCurrentUser();
        moduleinfo.setStatus(3);
        moduleinfo.setUpdatedBy(authUser);
        moduleInfoRepo.save(moduleinfo);
        return moduleInfoMapper.toDto(moduleinfo);
    }

    public int deleteBulkEntity(List<Long> entityIds) {
        return moduleInfoRepo.deleteBulkEntity(entityIds, 3L);
    }

    public ModuleInfoDto activateEntity(Long entityId) {
        ModuleInfo moduleinfo = moduleInfoRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("ModuleInfo not found with ID: " + entityId));
        var authUser = authService.getCurrentUser();
        moduleinfo.setStatus(1);
        moduleinfo.setUpdatedBy(authUser);
        moduleInfoRepo.save(moduleinfo);
        return moduleInfoMapper.toDto(moduleinfo);
    }

    public ModuleInfoDto deactivateEntity(Long entityId) {
        ModuleInfo moduleinfo = moduleInfoRepo.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Module not found with ID: " + entityId));
        var authUser = authService.getCurrentUser();
        moduleinfo.setStatus(2);
        moduleinfo.setUpdatedBy(authUser);
        moduleInfoRepo.save(moduleinfo);
        return moduleInfoMapper.toDto(moduleinfo);
    }
}
