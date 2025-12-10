package com.osudpotro.posmaster.resource.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api-resources")
public class ApiResourceController {
    private final ApiResourceService apiResourceService;

    @PreAuthorize("hasAuthority('RESOURCE_READ')")
    @GetMapping
    public List<ApiResourceDto> getAllApiResources(){
        return apiResourceService.getAllApiResources();
    }
    @GetMapping("/{id}")
    public ApiResourceDto getApiResource(@PathVariable Long id) {
        return apiResourceService.getApiResource(id);
    }
    @PostMapping
    public ResponseEntity<ApiResourceDto> createResource(@Valid @RequestBody ApiResourceCreateRequest request, UriComponentsBuilder uriBuilder){
        var resourceDto = apiResourceService.createApiResource(request);
        var uri=uriBuilder.path("/api-resources/{id}").buildAndExpand(resourceDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(resourceDto);
    }
    @PutMapping("/{id}")
    public ApiResourceDto updateResource(
            @PathVariable(name = "id") Long id,
            @RequestBody ApiResourceUpdateRequest request) {
        return apiResourceService.updateApiResource(id, request);
    }
    @DeleteMapping("/{id}")
    public ApiResourceDto deleteResource(
            @PathVariable(name = "id") Long id) {
        return apiResourceService.deleteApiResource(id);
    }

    @GetMapping("/{id}/activate")
    public ApiResourceDto activateResource(
            @PathVariable(name = "id") Long id) {
        return apiResourceService.activateApiResource(id);
    }

    @GetMapping("/{id}/deactivate")
    public ApiResourceDto deactivateResource(
            @PathVariable(name = "id") Long id) {
        return apiResourceService.deactivateApiResource(id);
    }


    @ExceptionHandler(DuplicateApiResourceException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateResource(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }
    @ExceptionHandler(ApiResourceNotFoundException.class)
    public ResponseEntity<Void> handleResourceNotFound() {
        return ResponseEntity.notFound().build();
    }
}
