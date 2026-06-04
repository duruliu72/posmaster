package com.osudpotro.posmaster.user.customer.address;

import com.osudpotro.posmaster.common.DuplicateEntityException;
import com.osudpotro.posmaster.common.EntityException;
import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.salecart.*;
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
@RequestMapping("/customer-address")
public class AddressController {
    private final AddressService addressService;

    //    @PreAuthorize("hasAuthority('CATEGORY_READ')")
    @GetMapping
    public List<AddressDto> getAllEntities() {
        return addressService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<AddressDto> getAllEntities(
            @RequestBody AddressFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AddressDto> result = addressService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    //    @PreAuthorize("hasAuthority('CATEGORY_READ')")
    @GetMapping("/{id}")
    public AddressDto getEntity(@PathVariable Long id) {
        return addressService.getEntity(id);
    }

    //    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    @PostMapping
    public ResponseEntity<AddressDto> createEntity(@Valid @RequestBody AddressCreateRequest request, UriComponentsBuilder uriBuilder) {
        var categoryDto = addressService.createEntity(request);
        var uri = uriBuilder.path("/customer-address/{id}").buildAndExpand(categoryDto.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryDto);
    }
    //    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    @PutMapping("/{id}")
    public AddressDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody AddressUpdateRequest request) {
        return addressService.updateEntity(id, request);
    }

    //    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    @DeleteMapping("/{id}")
    public AddressDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return addressService.deleteEntity(id);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEntityException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", "Name is already exist.")
        );
    }

    @ExceptionHandler(EntityException.class)
    public ResponseEntity<Map<String, String>> handleEntityException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFoundException() {
        return ResponseEntity.notFound().build();
    }
}