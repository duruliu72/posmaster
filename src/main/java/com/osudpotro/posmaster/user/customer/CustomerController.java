package com.osudpotro.posmaster.user.customer;

import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {


    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private final CustomerService customerService;





    @GetMapping("/search-customer")
    public ResponseEntity<?> searchCustomer(@RequestParam String keyword) {
        if (keyword == null || keyword.trim().length() < 2) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<Customer> customers = customerRepository.searchByEmailOrMobile(keyword.trim());

        List<Map<String, Object>> result = customers.stream()
                .map(c -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", c.getId());
                    map.put("userName", c.getUserName());
                    map.put("email", c.getEmail());
                    map.put("mobile", c.getMobile());

                    // Address
                    if (c.getAddresses() != null && !c.getAddresses().isEmpty()) {
                        var addr = c.getAddresses().get(0);
                        map.put("address", addr.getAddressType());
                        map.put("area", addr.getArea() != null ? addr.getArea().getName() : null);
                    }

                    // Membership
                    if (c.getMembership() != null) {
                        Map<String, Object> membershipMap = new HashMap<>();
                        membershipMap.put("id", c.getMembership().getId());
                        membershipMap.put("name", c.getMembership().getName());
                        map.put("membership", membershipMap);
                    }

                    return map;
                }).toList();

        return ResponseEntity.ok(result);
    }



    //    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerService.gerAllCustomers();
    }

    @PostMapping("/filter")
    public PagedResponse<CustomerDto> filterCustomers(
            @RequestBody CustomerFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CustomerDto> result = customerService.filterCustomers(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/filter-or")
    public PagedResponse<CustomerDto> orOpFilterCustomers(
            @RequestBody CustomerFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CustomerDto> result = customerService.orOpFilterCustomers(filter, pageable);
        return new PagedResponse<>(result);
    }
    //    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_CREATE')")
    @PostMapping
    public ResponseEntity<CustomerDto> registerCustomer(@Valid @RequestBody CustomerCreateRequest request, UriComponentsBuilder uriBuilder) {
        var customerDto = customerService.registerCustomer(request);
        var uri = uriBuilder.path("/customers/{id}").buildAndExpand(customerDto.getId()).toUri();
        return ResponseEntity.created(uri).body(customerDto);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_UPDATE')")
    @PutMapping("/{id}")
    public CustomerDto updateCustomer(
            @PathVariable(name = "id") Long id,
            @RequestBody CustomerUpdateRequest request) {
        return customerService.updateCustomer(id, request);
    }

    @PutMapping("/{id}/user")
    public CustomerDto updateUpdateEmailAndMobileForUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateForUserRequest request) {
        return customerService.updateUpdateEmailAndMobileForUser(id, request);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    @DeleteMapping("/{id}")
    public CustomerDto deleteCustomer(
            @PathVariable(name = "id") Long id) {
        return customerService.deleteCustomer(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkCustomer(@RequestBody CustomerBulkUpdateRequest request) {
        int count = customerService.deleteBulkCustomer(request.getCustomerIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    @GetMapping("/{id}/activate")
    public CustomerDto activateCustomer(
            @PathVariable(name = "id") Long id) {
        return customerService.activeCustomer(id);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    @GetMapping("/{id}/deactivate")
    public CustomerDto deactivateCustomer(
            @PathVariable(name = "id") Long id) {
        return customerService.deactivateCustomer(id);
    }


    @ExceptionHandler(DuplicateCustomerException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateCustomer(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("error", ex.getMessage())
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("error", ex.getMessage())
        );
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Void> handleCustomerNotFound() {
        return ResponseEntity.notFound().build();
    }
}