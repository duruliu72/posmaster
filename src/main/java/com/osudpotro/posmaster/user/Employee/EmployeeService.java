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
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("Email  is already registered");
        }
        if (request.getMobile() != null && userRepository.existsByMobile(request.getMobile())) {
            throw new DuplicateUserException("Mobile  is already registered");
        }
        Employee employee = employeeMapper.toEntity(request);
        var authUser = authService.getCurrentUser();
//        Common User Entity
        User user = employeeMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        var user = employee.getUser();
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            if (!user.getEmail().equals(request.getEmail())) {
                throw new DuplicateUserException("Email already exists");
            }
        }
        if (request.getMobile() != null && userRepository.existsByMobile(request.getMobile())) {
            if (!user.getMobile().equals(request.getMobile())) {
                throw new DuplicateUserException("Phone number already exists");
            }

        }
        if (request.getEmail() != null && request.getMobile() != null && userRepository.existsByEmailOrMobile(request.getEmail(), request.getMobile())) {
            if (!user.getEmail().equals(request.getEmail()) && !user.getMobile().equals(request.getMobile())) {
                throw new DuplicateUserException();
            }
        }
        employeeMapper.update(request, employee);
        employeeMapper.updateUser(request, user);
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        var authUser = authService.getCurrentUser();
        if (request.getMultimediaId() != null) {
            Multimedia multimedia = multimediaRepository.findById(request.getMultimediaId()).orElse(null);
            if (multimedia != null) {
                multimedia.setLinked(true);
                user.setProfilePic(multimedia);
            }
        }
        employee.setUser(user);
        employee.setUpdatedBy(authUser);
        employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    public EmployeeDto updateUpdateEmailAndMobileForUser(Long employeeId, UpdateEmailAndMobileForUserRequest request) {
        var employee = employeeRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        var user = userRepository.findById(employee.getUser().getId()).orElseThrow(UserNotFoundException::new);
        var authUser = authService.getCurrentUser();
        if (request.getEmail() != null) {
            if (!request.getEmail().equalsIgnoreCase(user.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateUserException("Email  is already registered");
            }
        }
        if (request.getMobile() != null) {
            if (!request.getMobile().equalsIgnoreCase(user.getMobile()) && userRepository.existsByMobile(request.getMobile())) {
                throw new DuplicateUserException("Mobile  is already registered");
            }
        }
        if (request.getUserName() != null) {
            user.setUserName(request.getUserName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getMobile() != null) {
            user.setMobile(request.getMobile());
        }
        employee.setUpdatedBy(authUser);
        user.setUpdatedBy(authUser);
        employee.setUser(user);
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
