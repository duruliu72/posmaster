package com.osudpotro.posmaster.branch;

import com.osudpotro.posmaster.auth.AuthService;
import com.osudpotro.posmaster.brand.Brand;
import com.osudpotro.posmaster.brand.BrandFilter;
import com.osudpotro.posmaster.brand.BrandSpecification;
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
    private final BranchMapper branchMapper;
    private final CsvReader csvReader;
    public List<BranchDto> getAllBranches() {
        return branchRepository.findAll()
                .stream()
                .map(branchMapper::toDto)
                .toList();
    }
    public BranchDto createBranch(BranchCreateRequest request){
        if(branchRepository.existsByName(request.getName())){
            throw new DuplicateBranchException();
        }
        var user = authService.getCurrentUser();
        var branch= branchMapper.toEntity(request);
        branch.setCreatedBy(user);
        branchRepository.save(branch);
        return branchMapper.toDto(branch);
    }
    public BranchDto updateBranch(Long branchId, BranchUpdateRequest request){
        var branch= branchRepository.findById(branchId).orElseThrow(BranchNotFoundException::new);
        var user = authService.getCurrentUser();
        branchMapper.update(request, branch);
        branch.setUpdatedBy(user);
        branchRepository.save(branch);
        return branchMapper.toDto(branch);
    }
    public BranchDto getBranch(Long branchId){
        var branch= branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
        return branchMapper.toDto(branch);
    }
    public Branch getBranchEntity(Long branchId){
        return branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
    }
    public BranchDto activateBranch(Long branchId){
        var branch = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
        var user = authService.getCurrentUser();
        branch.setStatus(1);
        branch.setUpdatedBy(user);
        branchRepository.save(branch);
        return branchMapper.toDto(branch);
    }
    public BranchDto deactivateBranch(Long branchId){
        var branch= branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
        var user=authService.getCurrentUser();
        branch.setStatus(2);
        branch.setUpdatedBy(user);
        branchRepository.save(branch);
        return branchMapper.toDto(branch);
    }
    public BranchDto deleteBranch(Long branchId){
        var branch= branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException("Branch not found with ID: " + branchId));
        var user=authService.getCurrentUser();
        branch.setStatus(3);
        branch.setUpdatedBy(user);
        branchRepository.save(branch);
        return branchMapper.toDto(branch);
    }

    public Page<BranchDto> getBranches(BranchFilter filter, Pageable pageable) {
        return branchRepository.findAll(BranchSpecification.filter(filter), pageable).map(branchMapper::toDto);
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