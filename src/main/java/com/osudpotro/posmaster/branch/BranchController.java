package com.osudpotro.posmaster.branch;

import com.osudpotro.posmaster.brand.BrandBulkUpdateRequest;
import com.osudpotro.posmaster.common.PagedResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/branches")
public class BranchController {
    private final BranchService branchService;
//    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<BranchDto> getAllBranches(){
        return branchService.getAllBranches();
    }
    @PostMapping("/filter")
    public PagedResponse<BranchDto> filterBrands(
            @RequestBody BranchFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<BranchDto> result = branchService.getBranches(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return branchService.importBranch(file);
    }
    @GetMapping("/{id}")
    public BranchDto getBranch(@PathVariable Long id) {
        return branchService.getBranch(id);
    }

    @PostMapping
    public ResponseEntity<BranchDto> createBranch(@Valid @RequestBody BranchCreateRequest request, UriComponentsBuilder uriBuilder){
        var branchDto = branchService.createBranch(request);
        var uri=uriBuilder.path("/branches/{id}").buildAndExpand(branchDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(branchDto);
    }
    @PutMapping("/{id}")
    public BranchDto updateBranch(
            @PathVariable(name = "id") Long id,
            @RequestBody BranchUpdateRequest request) {
        return branchService.updateBranch(id, request);
    }
    @DeleteMapping("/{id}")
    public BranchDto deleteBranch(
            @PathVariable(name = "id") Long id) {
        return branchService.deleteBranch(id);
    }
    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkBranch(@RequestBody BranchBulkUpdateRequest request) {
        var count = branchService.deleteBulkBranch(request.getBranchIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public BranchDto activateBranch(
            @PathVariable(name = "id") Long id) {
        return branchService.activateBranch(id);
    }

    @GetMapping("/{id}/deactivate")
    public BranchDto deactivateBranch(
            @PathVariable(name = "id") Long id) {
        return branchService.deactivateBranch(id);
    }


    @ExceptionHandler(DuplicateBranchException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateBranch(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }
    @ExceptionHandler(BranchNotFoundException.class)
    public ResponseEntity<Void> handleBranchNotFound() {
        return ResponseEntity.notFound().build();
    }

}