package com.osudpotro.posmaster.requisition;

import com.osudpotro.posmaster.common.PagedResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/requisition-on-paths")
public class RequisitionOnPathController {
    private final RequisitionOnPathService requisitionOnPathService;

    @GetMapping
    public List<RequisitionOnPathDto> getAllRequisitionOnPaths() {
        return requisitionOnPathService.getAllRequisitionOnPaths();
    }

    @GetMapping("/by_user")
    public List<RequisitionOnPathDto> getAllRequisitionsOnPathByUser() {
        return requisitionOnPathService.getAllRequisitionsOnPathByUser();
    }

    @PostMapping("/filter")
    public PagedResponse<RequisitionOnPathDto> filterRequisitionOnPaths(
            @RequestBody RequisitionOnPathFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RequisitionOnPathDto> result = requisitionOnPathService.filterRequisitionOnPaths(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/filter/by_user")
    public PagedResponse<RequisitionOnPathDto> filterRequisitionsOnPathByUser(
            @RequestBody RequisitionOnPathFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RequisitionOnPathDto> result = requisitionOnPathService.filterRequisitionsOnPathByUser(filter, pageable);
        return new PagedResponse<>(result);
    }

    @GetMapping("/{id}")
    public RequisitionOnPathDto getRequisitionOnPath(@PathVariable Long id) {
        return requisitionOnPathService.getRequisitionOnPath(id);
    }

    @GetMapping("/{id}/by_user")
    public RequisitionOnPathDto getRequisitionOnPathByUser(@PathVariable Long id) {
        return requisitionOnPathService.getRequisitionOnPath(id);
    }

    @PutMapping("/{id}")
    public RequisitionOnPathDto updateRequisitionOnPath(
            @PathVariable(name = "id") Long id,
            @RequestBody RequisitionOnPathServiceUpdateRequest request) {
        return requisitionOnPathService.updateRequisitionOnPath(id, request);
    }

    @ExceptionHandler(RequsitionOnPathNotFoundException.class)
    public ResponseEntity<Void> handleRequisitionOnPathNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(RequisitionOnPathAlreadyApprovedException.class)
    public ResponseEntity<Map<String, String>> handleRequisitionOnPathApprovedException(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("error", ex.getMessage())
        );
//        return ResponseEntity.badRequest().body(
//                Map.of("name", "Name is already exist.")
//        );
    }
}
