package com.osudpotro.posmaster.user.customer;

import com.osudpotro.posmaster.user.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class CustomerService {
    private final AuthService authService;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public List<CustomerDto> gerAllCustomers() {
        return customerRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(customerMapper::toDto)
                .toList();
    }

    public Page<CustomerDto> getCustomers(CustomerFilter filter, Pageable pageable) {
        return customerRepository.findAll(CustomerSpecification.filter(filter), pageable).map(customerMapper::toDto);
    }

    public CustomerDto CreateCustomer(CustomerCreateRequest request) {
        if (request.getEmail() != null && customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateCustomerException("Email already exists");
        }
        if (request.getMobile() != null && customerRepository.existsByMobile(request.getMobile())) {
            throw new DuplicateCustomerException("Phone number already exists");
        }
        if (request.getEmail() != null && request.getMobile() != null && customerRepository.existsByEmailOrMobile(request.getEmail(), request.getMobile())) {
            throw new DuplicateCustomerException();
        }
        var customer = customerMapper.toEntity(request);

        customer.setOtpRequestDateTime(LocalDateTime.now());

        var user = authService.getCurrentUser();
        customer.setCreatedBy(user);

        customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    public CustomerDto updateCustomer(Long customerId, CustomerUpdateRequest request) {
        var customer = customerRepository.findById(customerId).orElseThrow(CustomerNotFoundException::new);
        if (request.getEmail() != null && customerRepository.existsByEmail(request.getEmail())) {
            if (!customer.getEmail().equals(request.getEmail())) {
                throw new DuplicateCustomerException("Email already exists");
            }
        }
        if (request.getMobile() != null && customerRepository.existsByMobile(request.getMobile())) {
            if (!customer.getMobile().equals(request.getMobile())) {
                throw new DuplicateCustomerException("Phone number already exists");
            }

        }
        if (request.getEmail() != null && request.getMobile() != null && customerRepository.existsByEmailOrMobile(request.getEmail(), request.getMobile())) {
            if (!customer.getEmail().equals(request.getEmail()) && !customer.getMobile().equals(request.getMobile())) {
                throw new DuplicateCustomerException();
            }
        }
        customerMapper.update(request, customer);
        var user = authService.getCurrentUser();
        customer.setUpdatedBy(user);
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
