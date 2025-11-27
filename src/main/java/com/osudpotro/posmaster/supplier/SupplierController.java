package com.osudpotro.posmaster.supplier;

import com.osudpotro.posmaster.common.PagedResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
@AllArgsConstructor
@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierService supplierService;
    @GetMapping
    public List<SupplierDto> getAllSuppliers(){
        return supplierService.getAllSuppliers();
    }
    @PostMapping("/filter")
    public PagedResponse<SupplierDto> filterSuppliers(
            @RequestBody SupplierFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SupplierDto> result = supplierService.getSuppliers(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return supplierService.importSupplier(file);
    }
    @GetMapping("/{id}")
    public SupplierDto getSupplier(@PathVariable Long id) {
        return supplierService.getSupplier(id);
    }
    @PostMapping
    public ResponseEntity<SupplierDto> createSupplier(@Valid @RequestBody SupplierCreateRequest request, UriComponentsBuilder uriBuilder){
        var genericDto = supplierService.createSupplier(request);
        var uri=uriBuilder.path("/suppliers/{id}").buildAndExpand(genericDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(genericDto);
    }
    @PutMapping("/{id}")
    public SupplierDto updateSupplier(
            @PathVariable(name = "id") Long id,
            @RequestBody SupplierUpdateRequest request) {
        return supplierService.updateSupplier(id, request);
    }
    @DeleteMapping("/{id}")
    public SupplierDto deleteSupplier(
            @PathVariable(name = "id") Long id) {
        return supplierService.deleteSupplier(id);
    }
    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkOrganization(@RequestBody SupplierBulkUpdateRequest request) {
        var count = supplierService.deleteBulkSupplier(request.getSupplierIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }
    @GetMapping("/{id}/activate")
    public SupplierDto activateSupplier(
            @PathVariable(name = "id") Long id) {
        return supplierService.activeSupplier(id);
    }

    @GetMapping("/{id}/deactivate")
    public SupplierDto deactivateGeneric(
            @PathVariable(name = "id") Long id) {
        return supplierService.deactiveSupplier(id);
    }

    @ExceptionHandler(DuplicateSupplierException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateSupplier(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }
    @ExceptionHandler(SupplierNotFoundException.class)
    public ResponseEntity<Void> handleSupplierNotFound() {
        return ResponseEntity.notFound().build();
    }
}