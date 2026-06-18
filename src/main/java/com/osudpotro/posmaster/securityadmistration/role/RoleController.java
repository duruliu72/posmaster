package com.osudpotro.posmaster.securityadmistration.role;

import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.securityadmistration.resource.ResourceDto;
import com.osudpotro.posmaster.securityadmistration.resource.ResourceFilter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public List<RoleDto> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping("/filter")
    public PagedResponse<RoleDto> getAllEntities(
            @RequestBody RoleFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RoleDto> result = roleService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @GetMapping("/{id}")
    public RoleDto getRole(@PathVariable Long id) {
        return roleService.getRole(id);
    }

    @GetMapping("/{id}/permission-action")
    public RoleProjectionDto getRoleWithPermissionAction(@PathVariable Long id, @RequestParam(defaultValue = "0") Long moduleId) {
        Long moduleIdLoc = null;
        if (moduleId != 0) {
            moduleIdLoc = moduleId;
        }
        return roleService.getRoleWithPermissionAction(id, moduleIdLoc);
    }

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleCreateRequest request, UriComponentsBuilder uriBuilder) {
        var roleDto = roleService.createRole(request);
        var uri = uriBuilder.path("/roles/{id}").buildAndExpand(roleDto.getId()).toUri();
        return ResponseEntity.created(uri).body(roleDto);
    }

    @PutMapping("/{id}")
    public RoleDto updateRole(
            @PathVariable(name = "id") Long id,
            @RequestBody RoleUpdateRequest request) {
        return roleService.updateRole(id, request);
    }
    @PutMapping("/{id}/permission-action")
    public RoleProjectionDto updatePermissionAction(
            @PathVariable(name = "id") Long id,
            @RequestBody PermissionActionRequest request) {
        return roleService.updatePermissionAction(id, request);
    }
    @DeleteMapping("/{id}")
    public RoleDto deleteRole(
            @PathVariable(name = "id") Long id) {
        return roleService.deleteRole(id);
    }

    @GetMapping("/{id}/activate")
    public RoleDto activateRole(
            @PathVariable(name = "id") Long id) {
        return roleService.activeRole(id);
    }

    @GetMapping("/{id}/deactivate")
    public RoleDto deactivateRole(
            @PathVariable(name = "id") Long id) {
        return roleService.deActivateRole(id);
    }

    @ExceptionHandler(DuplicateRoleException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateRole(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Void> handleRoleNotFound() {
        return ResponseEntity.notFound().build();
    }
}
