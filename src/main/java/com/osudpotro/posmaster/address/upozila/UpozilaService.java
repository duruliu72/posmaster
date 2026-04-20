package com.osudpotro.posmaster.address.upozila;

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
public class UpozilaService {
    @Autowired
    private AuthService authService;
    @Autowired
    private UpozilaRepository upozilaRepo;
    @Autowired
    private UpozilaMapper upozilaMapper;
    @Autowired
    private DistrictRepository districtRepo;
    @Autowired
    private DivisionRepository divisionRepo;

    public List<UpozilaDto> getAllEntities() {
        return upozilaRepo.findAll()
                .stream()
                .map(upozilaMapper::toDto)
                .toList();
    }

    public Page<UpozilaDto> getAllEntities(UpozilaFilter filter, Pageable pageable) {
        return upozilaRepo.findAll(UpozilaSpecification.filter(filter), pageable).map(upozilaMapper::toDto);
    }

    public UpozilaDto getEntity(Long entityId) {
        var entity = upozilaRepo.findById(entityId).orElseThrow(() -> new UpozilaNotFoundException("Division not found with ID: " + entityId));
        return upozilaMapper.toDto(entity);
    }

    public UpozilaDto createEntity(UpozilaCreateRequest request) {
        if (upozilaRepo.existsByName(request.getName())) {
            throw new DuplicateUpozilaException();
        }
        Upozila upozila = new Upozila();
        var user = authService.getCurrentUser();
        upozila.setName(request.getName());
        upozila.setCreatedBy(user);
        if (request.getDistrictId() != null) {
            var district = districtRepo.findById(request.getDistrictId()).orElseThrow(() -> new DistrictNotFoundException("District not found with ID: " + request.getDistrictId()));
            upozila.setDistrict(district);
        }
        if (request.getDivisionId() != null) {
            var division = divisionRepo.findById(request.getDivisionId()).orElseThrow(() -> new DivisionNotFoundException("Division not found with ID: " + request.getDivisionId()));
            upozila.setDivision(division);
        }
        upozilaRepo.save(upozila);
        return upozilaMapper.toDto(upozila);
    }

    public UpozilaDto updateEntity(Long entityId, UpozilaUpdateRequest request) {
        var upozila = upozilaRepo.findById(entityId).orElseThrow(UpozilaNotFoundException::new);
        var user = authService.getCurrentUser();
        upozila.setName(request.getName());
        if (request.getDistrictId() != null) {
            var district = districtRepo.findById(request.getDistrictId()).orElseThrow(() -> new DistrictNotFoundException("District not found with ID: " + request.getDistrictId()));
            upozila.setDistrict(district);
        }
        if (request.getDivisionId() != null) {
            var division = divisionRepo.findById(request.getDivisionId()).orElseThrow(() -> new DivisionNotFoundException("Division not found with ID: " + request.getDivisionId()));
            upozila.setDivision(division);
        } else {
            upozila.setDivision(null);
        }
        upozila.setUpdatedBy(user);
        upozilaRepo.save(upozila);
        return upozilaMapper.toDto(upozila);
    }

    public UpozilaDto deleteEntity(Long entityId) {
        var district = upozilaRepo.findById(entityId).orElseThrow(() -> new DistrictNotFoundException("District not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        district.setStatus(3);
        district.setUpdatedBy(user);
        upozilaRepo.save(district);
        return upozilaMapper.toDto(district);
    }

    public int deleteBulkEntity(List<Long> entityIds) {
        return upozilaRepo.deleteBulkEntity(entityIds, 3L);
    }

    public UpozilaDto activateEntity(Long entityId) {
        var district = upozilaRepo.findById(entityId).orElseThrow(() -> new DistrictNotFoundException("District not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        district.setStatus(1);
        district.setUpdatedBy(user);
        upozilaRepo.save(district);
        return upozilaMapper.toDto(district);
    }

    public UpozilaDto deactivateEntity(Long entityId) {
        var district = upozilaRepo.findById(entityId).orElseThrow(() -> new DistrictNotFoundException("District not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        district.setStatus(2);
        district.setUpdatedBy(user);
        upozilaRepo.save(district);
        return upozilaMapper.toDto(district);
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
