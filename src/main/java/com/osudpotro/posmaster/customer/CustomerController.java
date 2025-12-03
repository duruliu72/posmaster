package com.osudpotro.posmaster.customer;

import com.osudpotro.posmaster.common.PagedResponse;
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
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    //    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerService.gerAllCustomers();
    }

    @PostMapping("/filter")
    public PagedResponse<CustomerDto> searchCustomers(
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
        Page<CustomerDto> result = customerService.getCustomers(filter, pageable);
        return new PagedResponse<>(result);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_CREATE')")
    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerCreateRequest request, UriComponentsBuilder uriBuilder) {
        var customerDto = customerService.CreateCustomer(request);
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
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Void> handleCustomerNotFound() {
        return ResponseEntity.notFound().build();
    }
}
