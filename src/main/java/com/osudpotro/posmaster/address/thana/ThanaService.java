package com.osudpotro.posmaster.address.thana;

import com.osudpotro.posmaster.address.district.DistrictNotFoundException;
import com.osudpotro.posmaster.address.district.DistrictRepository;
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
public class ThanaService {
    @Autowired
    private AuthService authService;
    @Autowired
    private ThanaRepository thanaRepo;
    @Autowired
    private ThanaMapper thanaMapper;
    @Autowired
    private DistrictRepository districtRepo;
    @Autowired
    private DivisionRepository divisionRepo;

    public List<ThanaDto> getAllEntities() {
        return thanaRepo.findAll()
                .stream()
                .map(thanaMapper::toDto)
                .toList();
    }

    public Page<ThanaDto> getAllEntities(ThanaFilter filter, Pageable pageable) {
        return thanaRepo.findAll(ThanaSpecification.filter(filter), pageable).map(thanaMapper::toDto);
    }

    public ThanaDto getEntity(Long entityId) {
        var entity = thanaRepo.findById(entityId).orElseThrow(() -> new ThanaNotFoundException("Division not found with ID: " + entityId));
        return thanaMapper.toDto(entity);
    }

    public ThanaDto createEntity(ThanaCreateRequest request) {
        if (thanaRepo.existsByName(request.getName())) {
            throw new DuplicateThanaException();
        }
        Thana thana = new Thana();
        var user = authService.getCurrentUser();
        thana.setName(request.getName());
        thana.setCreatedBy(user);
        if (request.getDistrictId() != null) {
            var district = districtRepo.findById(request.getDistrictId()).orElseThrow(() -> new DistrictNotFoundException("Division not found with ID: " + request.getDistrictId()));
            thana.setDistrict(district);
        }
        if (request.getDivisionId() != null) {
            var division = divisionRepo.findById(request.getDivisionId()).orElseThrow(() -> new DivisionNotFoundException("Division not found with ID: " + request.getDivisionId()));
            thana.setDivision(division);
        }
        thanaRepo.save(thana);
        return thanaMapper.toDto(thana);
    }

    public ThanaDto updateEntity(Long entityId, ThanaUpdateRequest request) {
        var thana = thanaRepo.findById(entityId).orElseThrow(ThanaNotFoundException::new);
        var user = authService.getCurrentUser();
        thana.setName(request.getName());
        if (request.getDistrictId() != null) {
            var district = districtRepo.findById(request.getDistrictId()).orElseThrow(() -> new DistrictNotFoundException("District not found with ID: " + request.getDistrictId()));
            thana.setDistrict(district);
        }
        if (request.getDivisionId() != null) {
            var division = divisionRepo.findById(request.getDivisionId()).orElseThrow(() -> new DivisionNotFoundException("Division not found with ID: " + request.getDivisionId()));
            thana.setDivision(division);
        } else {
            thana.setDivision(null);
        }
        thana.setUpdatedBy(user);
        thanaRepo.save(thana);
        return thanaMapper.toDto(thana);
    }

    public ThanaDto deleteEntity(Long entityId) {
        var district = thanaRepo.findById(entityId).orElseThrow(() -> new DistrictNotFoundException("District not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        district.setStatus(3);
        district.setUpdatedBy(user);
        thanaRepo.save(district);
        return thanaMapper.toDto(district);
    }

    public int deleteBulkEntity(List<Long> entityIds) {
        return thanaRepo.deleteBulkEntity(entityIds, 3L);
    }

    public ThanaDto activateEntity(Long entityId) {
        var district = thanaRepo.findById(entityId).orElseThrow(() -> new DistrictNotFoundException("District not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        district.setStatus(1);
        district.setUpdatedBy(user);
        thanaRepo.save(district);
        return thanaMapper.toDto(district);
    }

    public ThanaDto deactivateEntity(Long entityId) {
        var district = thanaRepo.findById(entityId).orElseThrow(() -> new DistrictNotFoundException("District not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        district.setStatus(2);
        district.setUpdatedBy(user);
        thanaRepo.save(district);
        return thanaMapper.toDto(district);
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
