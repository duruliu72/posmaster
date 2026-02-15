package com.osudpotro.posmaster.user.Employee;

import com.osudpotro.posmaster.user.DuplicateUserException;
import com.osudpotro.posmaster.user.UserMapper;
import com.osudpotro.posmaster.user.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
//    @PostMapping("/filter")
//    public PagedResponse<EmployeeDto> filterEmployees(
//            @RequestBody UserFilter filter,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id") String sortBy,
//            @RequestParam(defaultValue = "desc") String sortDir
//    ) {
//
//        Sort sort = sortDir.equalsIgnoreCase("asc") ?
//                Sort.by(sortBy).ascending() :
//                Sort.by(sortBy).descending();
//        Pageable pageable = PageRequest.of(page, size, sort);
//        Page<UserMainDto> result = employeeService.filterUsers(filter, pageable);
//        return new PagedResponse<>(result);
//    }

    //    @PostMapping("/filter-or")
//    public PagedResponse<UserMainDto> filterUsersOr(
//            @RequestBody UserFilter filter,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id") String sortBy,
//            @RequestParam(defaultValue = "desc") String sortDir
//    ) {
//        Sort sort = sortDir.equalsIgnoreCase("asc") ?
//                Sort.by(sortBy).ascending() :
//                Sort.by(sortBy).descending();
//        Pageable pageable = PageRequest.of(page, size, sort);
//        Page<UserMainDto> result = userService.filterForOrUsers(filter, pageable);
//        return new PagedResponse<>(result);
//    }
    @GetMapping("/{id}")
    public EmployeeDto getEmployee(@PathVariable Long id) {
        return employeeService.getEmployee(id);
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> registerEmployee(@Valid @RequestBody RegisterEmployeeRequest request, UriComponentsBuilder uriBuilder) {
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
