package com.osudpotro.posmaster.user.Employee;

import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.user.DuplicateUserException;
import com.osudpotro.posmaster.user.UserMapper;
import com.osudpotro.posmaster.user.UserRepository;
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
@RequestMapping("/employees")
public class EmployeeController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmployeeService employeeService;

    @GetMapping
    public List<EmployeeDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
    @PostMapping("/filter")
    public PagedResponse<EmployeeDto> filterEmployees(
            @RequestBody EmployeeFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<EmployeeDto> result = employeeService.filterEmployees(filter, pageable);
        return new PagedResponse<>(result);
    }
    @GetMapping("/{id}")
    public EmployeeDto getEmployee(@PathVariable Long id) {
        return employeeService.getEmployee(id);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> registerEmployee(@Valid @RequestBody EmployeeCreateRequest request, UriComponentsBuilder uriBuilder) {
        var employeeDto = employeeService.registerEmployee(request);
        var uri = uriBuilder.path("/employees/{id}").buildAndExpand(employeeDto.getId()).toUri();
        return ResponseEntity.created(uri).body(employeeDto);
    }

    @PutMapping("/{id}")
    public EmployeeDto updateEmployee(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateEmployeeRequest request) {
        return employeeService.updateEmployee(id, request);
    }

    @PutMapping("/{id}/user")
    public EmployeeDto updateUpdateEmailAndMobileForUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateEmailAndMobileForUserRequest request) {
        return employeeService.updateUpdateEmailAndMobileForUser(id, request);
    }
    //@PreAuthorize("hasAuthority('VEHICLE_DRIVER_DELETE')")
    @DeleteMapping("/{id}")
    public EmployeeDto deleteEmployee(
            @PathVariable(name = "id") Long id) {
        return employeeService.deleteEmployee(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkEmployee(@RequestBody EmployeeBulkUpdateRequest request) {
        int count = employeeService.deleteBulkEmployee(request.getEmployeeIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_DRIVER_DELETE')")
    @GetMapping("/{id}/activate")
    public EmployeeDto activateEmployee(
            @PathVariable(name = "id") Long id) {
        return employeeService.activeEmployee(id);
    }

    //    @PreAuthorize("hasAuthority('VEHICLE_DRIVER_DELETE')")
    @GetMapping("/{id}/deactivate")
    public EmployeeDto deactivateEmployee(
            @PathVariable(name = "id") Long id) {
        return employeeService.deactivateEmployee(id);
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateBranch(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Void> handleEmployeeNotFound() {
        return ResponseEntity.notFound().build();
    }
}
