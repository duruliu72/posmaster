package com.osudpotro.posmaster.role;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
    public List<RoleDto> getAllRoles(){
        return roleService.gerAllRoles();
    }
    @GetMapping("/{id}")
    public RoleDto getRole(@PathVariable Long id) {
        return roleService.getRole(id);
    }
    @PostMapping
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody RoleCreateRequest request, UriComponentsBuilder uriBuilder){
        var roleDto = roleService.createRole(request);
        var uri=uriBuilder.path("/roles/{id}").buildAndExpand(roleDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(roleDto);
    }
    @PutMapping("/{id}")
    public RoleDto updateRole(
            @PathVariable(name = "id") Long id,
            @RequestBody RoleUpdateRequest request) {
        System.out.println("jgjh");
        return roleService.updateRole(id, request);
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
