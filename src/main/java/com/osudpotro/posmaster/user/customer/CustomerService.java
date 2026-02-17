package com.osudpotro.posmaster.user.customer;

import com.osudpotro.posmaster.multimedia.Multimedia;
import com.osudpotro.posmaster.multimedia.MultimediaRepository;
import com.osudpotro.posmaster.role.Role;
import com.osudpotro.posmaster.role.RoleRepository;
import com.osudpotro.posmaster.user.*;
import com.osudpotro.posmaster.user.Employee.EmployeeNotFoundException;
import com.osudpotro.posmaster.user.auth.AuthService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class CustomerService {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final MultimediaRepository multimediaRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    public List<CustomerDto> gerAllCustomers() {
        return customerRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(customerMapper::toDto)
                .toList();
    }
    public Page<CustomerDto> filterCustomers(CustomerFilter filter, Pageable pageable) {
        return customerRepository.findAll(CustomerSpecification.filter(filter), pageable).map(customerMapper::toDto);
    }
    @Transactional
    public CustomerDto registerCustomer(CustomerCreateRequest request) {
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateCustomerException("Email already exists");
        }
        if (request.getMobile() != null && userRepository.existsByMobile(request.getMobile())) {
            throw new DuplicateCustomerException("Phone number already exists");
        }
        if (request.getEmail() != null && request.getMobile() != null && userRepository.existsByEmailOrMobile(request.getEmail(), request.getMobile())) {
            throw new DuplicateCustomerException();
        }
        var customer = customerMapper.toEntity(request);
        var authUser = authService.getCurrentUser();
        //Common User Entity
        User user = customerMapper.toUserEntity(request);
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setUserType(UserType.CUSTOMER);
        user.setCreatedBy(authUser);
        Role findRole = roleRepository.findByRoleKey("ROLE_CUSTOMER")
                .orElseGet(() -> {
                    Role superAdmin = new Role();
                    superAdmin.setName("Customer");
                    superAdmin.setRoleKey("ROLE_CUSTOMER");
                    superAdmin.setCreatedBy(authUser);
                    superAdmin.setUsers(new HashSet<>());
                    superAdmin.setPermissions(new HashSet<>());
                    return roleRepository.save(superAdmin);
                });
        // ===SET ROLE ADMIN USER  ===
        user.setRoles(Set.of(findRole));
        user = userRepository.save(user);
        customer.setUser(user);
        customer.setCreatedBy(authUser);
        customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }
    @Transactional
    public CustomerDto updateCustomer(Long customerId, CustomerUpdateRequest request) {
        var customer = customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        var user = customer.getUser();
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            if (!user.getEmail().equals(request.getEmail())) {
                throw new DuplicateCustomerException("Email already exists");
            }
        }
        if (request.getMobile() != null && userRepository.existsByMobile(request.getMobile())) {
            if (!user.getMobile().equals(request.getMobile())) {
                throw new DuplicateCustomerException("Phone number already exists");
            }

        }
        if (request.getEmail() != null && request.getMobile() != null && userRepository.existsByEmailOrMobile(request.getEmail(), request.getMobile())) {
            if (!user.getEmail().equals(request.getEmail()) && !user.getMobile().equals(request.getMobile())) {
                throw new DuplicateCustomerException();
            }
        }
        customerMapper.update(request, customer);
        customerMapper.updateUser(request, user);
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
        customer.setUser(user);
        customer.setUpdatedBy(authUser);
        customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }
    public CustomerDto updateUpdateEmailAndMobileForUser(Long employeeId, UpdateForUserRequest request) {
        var customer = customerRepository.findById(employeeId).orElseThrow(EmployeeNotFoundException::new);
        var user = userRepository.findById(customer.getUser().getId()).orElseThrow(UserNotFoundException::new);
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
        customer.setUpdatedBy(authUser);
        user.setUpdatedBy(authUser);
        customer.setUser(user);
        customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }
    public CustomerDto getCustomer(Long customerId) {
        var customer = customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        return customerMapper.toDto(customer);
    }

    public CustomerDto getCustomerOrNull(Long customerId) {
        var customer = customerRepository.findById(customerId).orElseThrow();
        return customerMapper.toDto(customer);
    }

    public Customer getCustomerEntity(Long customerId) {
        return customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
    }

    public CustomerDto activeCustomer(Long customerId) {
        var customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
        var user = authService.getCurrentUser();
        customer.setStatus(1);
        customer.setUpdatedBy(user);
        customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    public CustomerDto deactivateCustomer(Long customerId) {
        var customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
        var user = authService.getCurrentUser();
        customer.setStatus(2);
        customer.setUpdatedBy(user);
        customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    public CustomerDto deleteCustomer(Long customerId) {
        var customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
        var user = authService.getCurrentUser();
        customer.setStatus(3);
        customer.setUpdatedBy(user);
        customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    public int deleteBulkCustomer(List<Long> ids) {
        return customerRepository.deleteBulkCustomer(ids, 3L);
    }
}
