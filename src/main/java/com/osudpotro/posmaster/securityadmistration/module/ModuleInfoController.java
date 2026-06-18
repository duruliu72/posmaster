package com.osudpotro.posmaster.securityadmistration.module;

import com.osudpotro.posmaster.common.DuplicateEntityException;
import com.osudpotro.posmaster.common.EntityException;
import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.common.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/module-info")
public class ModuleInfoController {
    @Autowired
    private ModuleInfoService moduleInfoService;
    @GetMapping
    public List<ModuleInfoDto> getAllEntities() {
        return moduleInfoService.getAllEntities();
    }
    @PostMapping("/filter")
    public PagedResponse<ModuleInfoDto> getAllEntities(
            @RequestBody ModuleFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ModuleInfoDto> result = moduleInfoService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }
    @GetMapping("/{id}")
    public ModuleInfoDto getEntity(@PathVariable Long id) {
        return moduleInfoService.getEntity(id);
    }
    @PostMapping
    public ResponseEntity<ModuleInfoDto> createEntity(@Valid @RequestBody ModuleCreateRequest request, UriComponentsBuilder uriBuilder) {
        var entityDto = moduleInfoService.createEntity(request);
        var uri = uriBuilder.path("/branches/{id}").buildAndExpand(entityDto.getId()).toUri();
        return ResponseEntity.created(uri).body(entityDto);
    }
    @PutMapping("/{id}")
    public ModuleInfoDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody ModuleUpdateRequest request) {
        return moduleInfoService.updateEntity(id, request);
    }
    @DeleteMapping("/{id}")
    public ModuleInfoDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return moduleInfoService.deleteEntity(id);
    }
    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkEntity(@RequestBody ModuleBulkUpdateRequest request) {
        var count = moduleInfoService.deleteBulkEntity(request.getModuleIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }
    @GetMapping("/{id}/activate")
    public ModuleInfoDto activateEntity(
            @PathVariable(name = "id") Long id) {
        return moduleInfoService.activateEntity(id);
    }
    @GetMapping("/{id}/deactivate")
    public ModuleInfoDto deactivateEntity(
            @PathVariable(name = "id") Long id) {
        return moduleInfoService.deactivateEntity(id);
    }
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEntityException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }
    @ExceptionHandler(EntityException.class)
    public ResponseEntity<Map<String, String>> handleEntityException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFound() {
        return ResponseEntity.notFound().build();
    }

}