package com.osudpotro.posmaster.address.area;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class AreaService {
    public List<AreaDto> getAllEntities() {
//        return branchRepository.findAll()
//                .stream()
//                .map(customBranchMapper::toDto)
//                .toList();
        return null;
    }

    public Page<AreaDto> getAllEntities(AreaFilter filter, Pageable pageable) {
        return null;
//        return branchRepository.findAll(BranchSpecification.filter(filter), pageable).map(customBranchMapper::toDto);
    }

    public AreaDto getEntity(Long entityId) {
//        var branch = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
//        return customBranchMapper.toDto(branch);
        return null;
    }

    public AreaDto createEntity(AreaCreateRequest request) {
//        if (branchRepository.existsByName(request.getName())) {
//            throw new DuplicateBranchException();
//        }
//        var user = authService.getCurrentUser();
//        var branch = branchMapper.toEntity(request);
//        if (request.getMultimediaId() != null) {
//            Multimedia media = multimediaRepository.findById(request.getMultimediaId()).orElse(null);
//            if (media != null) {
//                media.setLinked(true);
//                branch.setMedia(media);
//            }
//        }
//        if (request.getOrganizationId() != null) {
//            Organization organization = organizationRepository.findById(request.getOrganizationId()).orElse(null);
//            if (organization != null) {
//                branch.setOrganization(organization);
//            }
//        }
//        branch.setCreatedBy(user);
//        branchRepository.save(branch);
//        return customBranchMapper.toDto(branch);
        return null;
    }

    public AreaDto updateEntity(Long entityId, AreaUpdateRequest request) {
//        var branch = branchRepository.findById(branchId).orElseThrow(BranchNotFoundException::new);
//        var user = authService.getCurrentUser();
//        branchMapper.update(request, branch);
//        if (request.getMultimediaId() != null) {
//            Multimedia media = multimediaRepository.findById(request.getMultimediaId()).orElse(null);
//            if (media != null) {
//                media.setLinked(true);
//                branch.setMedia(media);
//            }
//        }
//        branch.setOrganization(null);
//        if (request.getOrganizationId() != null) {
//            Organization organization = organizationRepository.findById(request.getOrganizationId()).orElse(null);
//            if (organization != null) {
//                branch.setOrganization(organization);
//            }
//        }
//        branch.setUpdatedBy(user);
//        branchRepository.save(branch);
//        return customBranchMapper.toDto(branch);
        return null;
    }

    public AreaDto deleteEntity(Long entityId) {
//        var branch = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
//        var user = authService.getCurrentUser();
//        branch.setStatus(3);
//        branch.setUpdatedBy(user);
//        branchRepository.save(branch);
//        return customBranchMapper.toDto(branch);
        return null;
    }

    public int deleteBulkEntity(List<Long> entityId) {
//        return branchRepository.deleteBulkBranch(branchIds, 3L);
        return 0;
    }

    public AreaDto activateEntity(Long entityId) {
//        var branch = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
//        var user = authService.getCurrentUser();
//        branch.setStatus(1);
//        branch.setUpdatedBy(user);
//        branchRepository.save(branch);
//        return customBranchMapper.toDto(branch);
        return null;
    }

    public AreaDto deactivateEntity(Long entityId) {
//        var branch = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
//        var user = authService.getCurrentUser();
//        branch.setStatus(2);
//        branch.setUpdatedBy(user);
//        branchRepository.save(branch);
//        return customBranchMapper.toDto(branch);
        return null;
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
