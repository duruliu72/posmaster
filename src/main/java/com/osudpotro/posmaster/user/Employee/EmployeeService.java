package com.osudpotro.posmaster.user.Employee;

import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.multimedia.MultimediaRepository;
import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.role.RoleRepository;
import com.osudpotro.posmaster.user.*;
import com.osudpotro.posmaster.user.auth.AuthService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class EmployeeService {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final MultimediaRepository multimediaRepository;
    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;

    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    public Page<EmployeeDto> filterEmployees(EmployeeFilter filter, Pageable pageable) {
        return employeeRepository.findAll(EmployeeSpecification.filter(filter), pageable).map(employeeMapper::toDto);
    }

    @Transactional
    public EmployeeDto registerEmployee(EmployeeCreateRequest request) {
        if (request.getEmail() != null && employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("Email  is already registered");
        }
        if (request.getMobile() != null && employeeRepository.existsByMobile(request.getMobile())) {
            throw new DuplicateUserException("Mobile  is already registered");
        }
        Employee employee = employeeMapper.toEntity(request);
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            employee.setPassword(employee.getPassword());
        }
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        var authUser = authService.getCurrentUser();
//        Common User Entity
        User user = new User();
        user.setUserType(UserType.EMPLOYEE);
        user.setCreatedBy(authUser);
        Role findRole = roleRepository.findByRoleKey("ROLE_EMPLOYEE")
                .orElseGet(() -> {
                    Role superAdmin = new Role();
                    superAdmin.setName("Employee");
                    superAdmin.setRoleKey("ROLE_EMPLOYEE");
                    superAdmin.setCreatedBy(authUser);
                    superAdmin.setUsers(new HashSet<>());
                    superAdmin.setPermissions(new HashSet<>());
                    return roleRepository.save(superAdmin);
                });
        // ===SET ROLE ADMIN USER  ===
        user.setRoles(Set.of(findRole));
        user = userRepository.save(user);
        employee.setUser(user);
        employee.setCreatedBy(authUser);
        employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    @Transactional
    public EmployeeDto updateEmployee(Long employeeId, UpdateEmployeeRequest request) {
        var employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        if (request.getEmail() != null && employeeRepository.existsByEmail(request.getEmail())) {
            if (!employee.getEmail().equals(request.getEmail())) {
                throw new DuplicateUserException("Email already exists");
            }
        }
        if (request.getMobile() != null && employeeRepository.existsByMobile(request.getMobile())) {
            if (!employee.getMobile().equals(request.getMobile())) {
                throw new DuplicateUserException("Phone number already exists");
            }

        }
        if (request.getEmail() != null && request.getMobile() != null && employeeRepository.existsByEmailOrMobile(request.getEmail(), request.getMobile())) {
            if (!employee.getEmail().equals(request.getEmail()) && !employee.getMobile().equals(request.getMobile())) {
                throw new DuplicateUserException();
            }
        }
        employeeMapper.update(request, employee);
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        }
        var authUser = authService.getCurrentUser();
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        if (request.getMultimediaId() != null) {
            Multimedia multimedia = multimediaRepository.findById(request.getMultimediaId()).orElse(null);
            if (multimedia != null) {
                multimedia.setLinked(true);
                employee.setProfilePic(multimedia);
            }
        }
        employee.setUpdatedBy(authUser);
        employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    public EmployeeDto updateUpdateEmailAndMobileForUser(Long employeeId, UpdateEmailAndMobileForUserRequest request) {
        var employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        var user = userRepository.findById(employee.getUser().getId()).orElseThrow(UserNotFoundException::new);
        var authUser = authService.getCurrentUser();
        if (request.getEmail() != null) {
            if (!request.getEmail().equalsIgnoreCase(employee.getEmail()) && employeeRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateUserException("Email  is already registered");
            }
            employee.setEmail(request.getEmail());
        }
        if (request.getMobile() != null) {
            if (!request.getMobile().equalsIgnoreCase(employee.getMobile()) && employeeRepository.existsByMobile(request.getMobile())) {
                throw new DuplicateUserException("Mobile  is already registered");
            }
            employee.setMobile(request.getMobile());
        }
        if (request.getUserName() != null) {
            employee.setUserName(request.getUserName());
        }
        employee.setUpdatedBy(authUser);
        user.setUpdatedBy(authUser);
        userRepository.save(user);
        employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    public EmployeeDto getEmployee(Long employeeId) {
        var employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException("Employee User not found with ID: " + employeeId));
        return employeeMapper.toDto(employee);
    }
    public EmployeeDto activeEmployee(Long customerId) {
        var vehicleDriver = employeeRepository.findById(customerId).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + customerId));
        var authUser = authService.getCurrentUser();
        vehicleDriver.setStatus(1);
        vehicleDriver.setUpdatedBy(authUser);
        employeeRepository.save(vehicleDriver);
        return employeeMapper.toDto(vehicleDriver);
    }

    public EmployeeDto deactivateEmployee(Long customerId) {
        var vehicleDriver = employeeRepository.findById(customerId).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + customerId));
        var authUser = authService.getCurrentUser();
        vehicleDriver.setStatus(2);
        vehicleDriver.setUpdatedBy(authUser);
        employeeRepository.save(vehicleDriver);
        return employeeMapper.toDto(vehicleDriver);
    }

    public EmployeeDto deleteEmployee(Long customerId) {
        var vehicleDriver = employeeRepository.findById(customerId).orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + customerId));
        var authUser = authService.getCurrentUser();
        vehicleDriver.setStatus(3);
        vehicleDriver.setUpdatedBy(authUser);
        employeeRepository.save(vehicleDriver);
        return employeeMapper.toDto(vehicleDriver);
    }
    public int deleteBulkEmployee(List<Long> ids) {
        return employeeRepository.deleteBulkEmployee(ids, 3L);
    }
}
