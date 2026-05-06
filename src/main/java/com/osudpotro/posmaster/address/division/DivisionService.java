package com.osudpotro.posmaster.address.division;

import com.osudpotro.posmaster.user.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DivisionService {
    @Autowired
    private AuthService authService;
    @Autowired
    private DivisionRepository divisionRepo;
    @Autowired
    private DivisionMapper divisionMapper;

    public List<DivisionDto> getAllEntities() {
        return divisionRepo.findAll()
                .stream()
                .map(divisionMapper::toDto)
                .toList();
    }

    public Page<DivisionDto> getAllEntities(DivisionFilter filter, Pageable pageable) {
        return divisionRepo.findAll(DivisionSpecification.filter(filter), pageable).map(divisionMapper::toDto);
    }

    public DivisionDto getEntity(Long entityId) {
        var entity = divisionRepo.findById(entityId).orElseThrow(() -> new DivisionNotFoundException("Division not found with ID: " + entityId));
        return divisionMapper.toDto(entity);
    }

    public DivisionDto createEntity(DivisionCreateRequest request) {
        if (divisionRepo.existsByName(request.getName())) {
            throw new DuplicateDivisionException();
        }
        var user = authService.getCurrentUser();
        Division division = new Division();
        division.setName(request.getName());
        division.setCreatedBy(user);
        divisionRepo.save(division);
        return divisionMapper.toDto(division);
    }

    public DivisionDto updateEntity(Long entityId, DivisionUpdateRequest request) {
        var division = divisionRepo.findById(entityId).orElseThrow(DivisionNotFoundException::new);
        var user = authService.getCurrentUser();
        division.setName(request.getName());
        division.setUpdatedBy(user);
        divisionRepo.save(division);
        return divisionMapper.toDto(division);
    }

    public DivisionDto deleteEntity(Long entityId) {
        var division = divisionRepo.findById(entityId).orElseThrow(() -> new DivisionNotFoundException("Division not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        division.setStatus(3);
        division.setUpdatedBy(user);
        divisionRepo.save(division);
        return divisionMapper.toDto(division);
    }

    public int deleteBulkEntity(List<Long> entityIds) {
        return divisionRepo.deleteBulkEntity(entityIds, 3L);
    }

    public DivisionDto activateEntity(Long entityId) {
        var division = divisionRepo.findById(entityId).orElseThrow(() -> new DivisionNotFoundException("Division not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        division.setStatus(1);
        division.setUpdatedBy(user);
        divisionRepo.save(division);
        return divisionMapper.toDto(division);
    }

    public DivisionDto deactivateEntity(Long entityId) {
        var division = divisionRepo.findById(entityId).orElseThrow(() -> new DivisionNotFoundException("Division not found with ID: " + entityId));
        var user = authService.getCurrentUser();
        division.setStatus(2);
        division.setUpdatedBy(user);
        divisionRepo.save(division);
        return divisionMapper.toDto(division);
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
