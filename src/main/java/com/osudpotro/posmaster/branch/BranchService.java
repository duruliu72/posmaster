package com.osudpotro.posmaster.branch;

import com.osudpotro.posmaster.user.auth.AuthService;
import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.multimedia.MultimediaRepository;
import com.osudpotro.posmaster.organization.Organization;
import com.osudpotro.posmaster.organization.OrganizationRepository;
import com.osudpotro.posmaster.utility.CsvReader;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class BranchService {
    private final AuthService authService;
    private final BranchRepository branchRepository;
    private final OrganizationRepository organizationRepository;
    private final MultimediaRepository multimediaRepository;
    private final BranchMapper branchMapper;
    private final CustomBranchMapper customBranchMapper;
    private final CsvReader csvReader;

    public List<BranchDto> getAllBranches() {
        return branchRepository.findAll()
                .stream()
                .map(customBranchMapper::toDto)
                .toList();
    }

    public BranchDto createBranch(BranchCreateRequest request) {
        if (branchRepository.existsByName(request.getName())) {
            throw new DuplicateBranchException();
        }
        var user = authService.getCurrentUser();
        var branch = branchMapper.toEntity(request);
        if (request.getMultimediaId() != null) {
            Multimedia media = multimediaRepository.findById(request.getMultimediaId()).orElse(null);
            if (media != null) {
                media.setLinked(true);
                branch.setMedia(media);
            }
        }
        if (request.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(request.getOrganizationId()).orElse(null);
            if (organization != null) {
                branch.setOrganization(organization);
            }
        }
        branch.setCreatedBy(user);
        branchRepository.save(branch);
        return customBranchMapper.toDto(branch);
    }

    public BranchDto updateBranch(Long branchId, BranchUpdateRequest request) {
        var branch = branchRepository.findById(branchId).orElseThrow(BranchNotFoundException::new);
        var user = authService.getCurrentUser();
        branchMapper.update(request, branch);
        if (request.getMultimediaId() != null) {
            Multimedia media = multimediaRepository.findById(request.getMultimediaId()).orElse(null);
            if (media != null) {
                media.setLinked(true);
                branch.setMedia(media);
            }
        }
        branch.setOrganization(null);
        if (request.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(request.getOrganizationId()).orElse(null);
            if (organization != null) {
                branch.setOrganization(organization);
            }
        }
        branch.setUpdatedBy(user);
        branchRepository.save(branch);
        return customBranchMapper.toDto(branch);
    }

    public BranchDto getBranch(Long branchId) {
        var branch = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
        return customBranchMapper.toDto(branch);
    }

    public Branch getBranchEntity(Long branchId) {
        return branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
    }

    public BranchDto activateBranch(Long branchId) {
        var branch = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
        var user = authService.getCurrentUser();
        branch.setStatus(1);
        branch.setUpdatedBy(user);
        branchRepository.save(branch);
        return customBranchMapper.toDto(branch);
    }

    public BranchDto deactivateBranch(Long branchId) {
        var branch = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
        var user = authService.getCurrentUser();
        branch.setStatus(2);
        branch.setUpdatedBy(user);
        branchRepository.save(branch);
        return customBranchMapper.toDto(branch);
    }

    public BranchDto deleteBranch(Long branchId) {
        var branch = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
        var user = authService.getCurrentUser();
        branch.setStatus(3);
        branch.setUpdatedBy(user);
        branchRepository.save(branch);
        return customBranchMapper.toDto(branch);
    }

    public Page<BranchDto> getBranches(BranchFilter filter, Pageable pageable) {
        return branchRepository.findAll(BranchSpecification.filter(filter), pageable).map(customBranchMapper::toDto);
    }

    public int importBranch(MultipartFile file) {
        var user = authService.getCurrentUser();
        List<String[]> rows = csvReader.readCSV(file);
        boolean hasHeader = true;
        int count = 0;
        List<Branch> branches = new ArrayList<>();
        for (int i = hasHeader ? 1 : 0; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            // Expecting: name, description
            String name = cols.length > 0 ? cols[0] : null;
            if (name == null || name.trim().isEmpty()) {
                continue; // Skip invalid rows
            }
            Branch branch = new Branch();
            branch.setName(name.trim());
            branch.setCreatedBy(user);
            branches.add(branch);
            count++;
        }
        branchRepository.saveAll(branches);
        return count;
    }

    public int deleteBulkBranch(List<Long> branchIds) {
        return branchRepository.deleteBulkBranch(branchIds, 3L);
    }
}