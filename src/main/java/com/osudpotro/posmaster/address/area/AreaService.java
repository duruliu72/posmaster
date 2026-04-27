package com.osudpotro.posmaster.address.area;

import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class AreaService {
    @Autowired
    private AuthService authService;
    @Autowired
    private AreaRepository areaRepo;
    @Autowired
    private AreaMapper areaMapper;

    public List<AreaDto> getAllEntities() {
        return areaRepo.findAll()
                .stream()
                .map(areaMapper::toDto)
                .toList();
    }

    public Page<AreaDto> getAllEntities(AreaFilter filter, Pageable pageable) {
        return areaRepo.findAll(AreaSpecification.filter(filter), pageable).map(areaMapper::toDto);
    }

    public AreaDto getEntity(Long entityId) {
        var entity = areaRepo.findById(entityId).orElseThrow(() -> new AreaNotFoundException("Area not found with ID: " + entityId));
        return areaMapper.toDto(entity);
    }

    public AreaDto createEntity(AreaCreateRequest request) {
        if (areaRepo.existsByName(request.getName())) {
            throw new DuplicateAreaException();
        }
        if(request.getParentAreaId()==null&& request.getIsSubArea()){
            throw new AreaException("Bad information");
        }
        var user = authService.getCurrentUser();
        Area area = new Area();
        area.setName(request.getName());
        if (request.getParentAreaId() != null) {
            var parentArea = areaRepo.findById(request.getParentAreaId()).orElseThrow(AreaNotFoundException::new);
            area.setParentArea(parentArea);
        }
        if (request.getIsSubArea() != null) {
            area.setIsSubArea(request.getIsSubArea());
        }
        area.setCreatedBy(user);
        areaRepo.save(area);
        return areaMapper.toDto(area);
    }

    public AreaDto updateEntity(Long entityId, AreaUpdateRequest request) {
        var area = areaRepo.findById(entityId).orElseThrow(AreaNotFoundException::new);
        if(request.getParentAreaId()==null&& request.getIsSubArea()){
            throw new AreaException("Bad information");
        }
        if(Objects.equals(request.getParentAreaId(), area.getId())){
          throw new AreaException("Bad Parent information");
        }
        var user = authService.getCurrentUser();
        area.setName(request.getName());
        if (request.getParentAreaId() != null) {
            var parentArea = areaRepo.findById(request.getParentAreaId()).orElseThrow(AreaNotFoundException::new);
            area.setParentArea(parentArea);
        }
        if (request.getIsSubArea() != null) {
            area.setIsSubArea(request.getIsSubArea());
        }
        area.setUpdatedBy(user);
        areaRepo.save(area);
        return areaMapper.toDto(area);
    }

    public AreaDto deleteEntity(Long entityId) {
        var area = areaRepo.findById(entityId).orElseThrow(() -> new AreaNotFoundException("Area not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        area.setStatus(3);
        area.setUpdatedBy(user);
        areaRepo.save(area);
        return areaMapper.toDto(area);
    }

    public int deleteBulkEntity(List<Long> entityIds) {
        return areaRepo.deleteBulkEntity(entityIds, 3L);
    }

    public AreaDto activateEntity(Long entityId) {
        var area = areaRepo.findById(entityId).orElseThrow(() -> new AreaNotFoundException("Area not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        area.setStatus(1);
        area.setUpdatedBy(user);
        areaRepo.save(area);
        return areaMapper.toDto(area);
    }

    public AreaDto deactivateEntity(Long entityId) {
        var branch = areaRepo.findById(entityId).orElseThrow(() -> new AreaNotFoundException("Area not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        branch.setStatus(2);
        branch.setUpdatedBy(user);
        areaRepo.save(branch);
        return areaMapper.toDto(branch);
    }

    public int importEntities(MultipartFile file) {
//        var user = authService.getCurrentUser();
//        List<String[]> rows = csvReader.readCSV(file);
//        boolean hasHeader = true;
//        int count = 0;
//        List<Branch> branches = new ArrayList<>();
//        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
//            String[] cols = rows.get(i);
//            // Expecting: name, description
//            String name = cols.length > 0 ? cols[0] : null;
//            if (name == null || name.trim().isEmpty()) {
//                continue; // Skip invalid rows
//            }
//            Branch branch = new Branch();
//            branch.setName(name.trim());
//            branch.setCreatedBy(user);
//            branches.add(branch);
//            count++;
//        }
//        branchRepository.saveAll(branches);
        return 0;
    }
}
