package com.osudpotro.posmaster.address.district;

import com.osudpotro.posmaster.address.division.DivisionNotFoundException;
import com.osudpotro.posmaster.address.division.DivisionRepository;
import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DistrictService {
    @Autowired
    private AuthService authService;
    @Autowired
    private DistrictRepository districtRepo;
    @Autowired
    private DistrictMapper districtMapper;
    @Autowired
    private DivisionRepository divisionRepo;

    public List<DistrictDto> getAllEntities() {
        return districtRepo.findAll()
                .stream()
                .map(districtMapper::toDto)
                .toList();
    }

    public Page<DistrictDto> getAllEntities(DistrictFilter filter, Pageable pageable) {
        return districtRepo.findAll(DistrictSpecification.filter(filter), pageable).map(districtMapper::toDto);
    }

    public DistrictDto getEntity(Long entityId) {
        var entity = districtRepo.findById(entityId).orElseThrow(() -> new DistrictNotFoundException("Division not found with ID: " + entityId));
        return districtMapper.toDto(entity);
    }

    public DistrictDto createEntity(DistrictCreateRequest request) {
        if (districtRepo.existsByName(request.getName())) {
            throw new DuplicateDistrictException();
        }
        District district = new District();
        var user = authService.getCurrentUser();
        district.setName(request.getName());
        district.setCreatedBy(user);
        if (request.getDivisionId() != null) {
            var division = divisionRepo.findById(request.getDivisionId()).orElseThrow(() -> new DivisionNotFoundException("Division not found with ID: " + request.getDivisionId()));
            district.setDivision(division);
        }
        districtRepo.save(district);
        return districtMapper.toDto(district);
    }

    public DistrictDto updateEntity(Long entityId, DistrictUpdateRequest request) {
        var district = districtRepo.findById(entityId).orElseThrow(DistrictNotFoundException::new);
        var user = authService.getCurrentUser();
        district.setName(request.getName());
        if (request.getDivisionId() != null) {
            var division = divisionRepo.findById(request.getDivisionId()).orElseThrow(() -> new DivisionNotFoundException("Division not found with ID: " + request.getDivisionId()));
            district.setDivision(division);
        } else {
            district.setDivision(null);
        }
        district.setUpdatedBy(user);
        districtRepo.save(district);
        return districtMapper.toDto(district);
    }

    public DistrictDto deleteEntity(Long entityId) {
        var district = districtRepo.findById(entityId).orElseThrow(() -> new DistrictNotFoundException("District not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        district.setStatus(3);
        district.setUpdatedBy(user);
        districtRepo.save(district);
        return districtMapper.toDto(district);
    }

    public int deleteBulkEntity(List<Long> entityIds) {
        return districtRepo.deleteBulkEntity(entityIds, 3L);
    }

    public DistrictDto activateEntity(Long entityId) {
        var district = districtRepo.findById(entityId).orElseThrow(() -> new DistrictNotFoundException("District not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        district.setStatus(1);
        district.setUpdatedBy(user);
        districtRepo.save(district);
        return districtMapper.toDto(district);
    }

    public DistrictDto deactivateEntity(Long entityId) {
        var district = districtRepo.findById(entityId).orElseThrow(() -> new DistrictNotFoundException("District not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        district.setStatus(2);
        district.setUpdatedBy(user);
        districtRepo.save(district);
        return districtMapper.toDto(district);
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
