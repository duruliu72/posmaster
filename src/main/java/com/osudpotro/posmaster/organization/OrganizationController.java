package com.osudpotro.posmaster.organization;

import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.manufacturer.ManufacturerBulkUpdateRequest;
import com.osudpotro.posmaster.manufacturer.ManufacturerDto;
import com.osudpotro.posmaster.manufacturer.ManufacturerFilter;
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
@RequestMapping("/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;
    @GetMapping
    public List<OrganizationDto> getAllOrganizations(){
        return organizationService.getAllOrganizations();
    }
    @PostMapping("/filter")
    public PagedResponse<OrganizationDto> filterManufacturers(
            @RequestBody OrganizationFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<OrganizationDto> result = organizationService.getOrganizations(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return organizationService.importOrganization(file);
    }
    @GetMapping("/{id}")
    public OrganizationDto getOrganization(@PathVariable Long id) {
        return organizationService.getOrganization(id);
    }
    @PostMapping
    public ResponseEntity<OrganizationDto> createOrganization(@Valid @RequestBody OrganizationCreateRequest request, UriComponentsBuilder uriBuilder){
        var genericDto = organizationService.createOrganization(request);
        var uri=uriBuilder.path("/manufacturers/{id}").buildAndExpand(genericDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(genericDto);
    }
    @PutMapping("/{id}")
    public OrganizationDto updateOrganization(
            @PathVariable(name = "id") Long id,
            @RequestBody OrganizationUpdateRequest request) {
        return organizationService.updateOrganization(id, request);
    }
    @DeleteMapping("/{id}")
    public OrganizationDto deleteOrganization(
            @PathVariable(name = "id") Long id) {
        return organizationService.deleteOrganization(id);
    }
    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkOrganization(@RequestBody OrganizationBulkUpdateRequest request) {
        var count = organizationService.deleteBulkOrganization(request.getOrganizationIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }
    @GetMapping("/{id}/activate")
    public OrganizationDto activateOrganization(
            @PathVariable(name = "id") Long id) {
        return organizationService.activeOrganization(id);
    }

    @GetMapping("/{id}/deactivate")
    public OrganizationDto deactivateGeneric(
            @PathVariable(name = "id") Long id) {
        return organizationService.deactivateOrganization(id);
    }

    @ExceptionHandler(DuplicateOrganizationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateOrganization(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }
    @ExceptionHandler(OrganizationNotFoundException.class)
    public ResponseEntity<Void> handleOrganizationNotFound() {
        return ResponseEntity.notFound().build();
    }
}