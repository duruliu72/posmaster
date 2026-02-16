package com.osudpotro.posmaster.user.admin;

import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.user.*;
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

@RestController
@AllArgsConstructor
@RequestMapping("/admin-users")
public class AdminUserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AdminUserService adminUserService;

    @GetMapping
    public List<AdminUserDto> getAllAdminUsers() {
        return adminUserService.getAllAdminUsers();
    }
    @PostMapping("/filter")
    public PagedResponse<AdminUserDto> filterAdminUsers(
            @RequestBody AdminUserFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AdminUserDto> result = adminUserService.filterAdminUsers(filter, pageable);
        return new PagedResponse<>(result);
    }
    @GetMapping("/{id}")
    public AdminUserDto getAdminUser(@PathVariable Long id) {
        return adminUserService.getAdminUser(id);
    }

    @PostMapping
    public ResponseEntity<AdminUserDto> registerAdminUser(@Valid @RequestBody AdminUserCreateRequest request, UriComponentsBuilder uriBuilder) {
        var userDto = adminUserService.registerAdminUser(request);
        var uri = uriBuilder.path("/admin-users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public AdminUserDto updateAdminUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateAdminUserRequest request) {
        return adminUserService.updateAdminUser(id, request);
    }

    @PutMapping("/{id}/user")
    public AdminUserDto updateUpdateEmailAndMobileForUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateEmailAndMobileForUserRequest request) {
        return adminUserService.updateUpdateEmailAndMobileForUser(id, request);
    }
    //@PreAuthorize("hasAuthority('VEHICLE_DRIVER_DELETE')")
    @DeleteMapping("/{id}")
    public AdminUserDto deleteAdminUser(
            @PathVariable(name = "id") Long id) {
        return adminUserService.deleteAdminUser(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkAdminUser(@RequestBody AdminUserBulkUpdateRequest request) {
        int count = adminUserService.deleteBulkAdminUser(request.getAdminUserIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_DRIVER_DELETE')")
    @GetMapping("/{id}/activate")
    public AdminUserDto activateAdminUser(
            @PathVariable(name = "id") Long id) {
        return adminUserService.activeAdminUser(id);
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_DRIVER_DELETE')")
    @GetMapping("/{id}/deactivate")
    public AdminUserDto deactivateAdminUser(
            @PathVariable(name = "id") Long id) {
        return adminUserService.deactivateAdminUser(id);
    }


    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateBranch(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }

    @ExceptionHandler(AdminUserNotFoundException.class)
    public ResponseEntity<Void> handleAdminUserNotFound() {
        return ResponseEntity.notFound().build();
    }
}
