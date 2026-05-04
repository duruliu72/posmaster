package com.osudpotro.posmaster.salecart;

import com.osudpotro.posmaster.common.DuplicateEntityException;
import com.osudpotro.posmaster.common.EntityException;
import com.osudpotro.posmaster.common.EntityNotFoundException;
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
@RequestMapping("/sale-carts")
public class SaleCartController {
    private final SaleCartService saleCartService;

    //    @PreAuthorize("hasAuthority('CATEGORY_READ')")
    @GetMapping
    public List<SaleCartDto> getAllEntities() {
        return saleCartService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<SaleCartDto> getAllEntities(
            @RequestBody SaleCartFilter filter,
//            @RequestParam Map<String, String> params,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SaleCartDto> result = saleCartService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    //    @PreAuthorize("hasAuthority('CATEGORY_READ')")
    @GetMapping("/{id}")
    public SaleCartDto getEntity(@PathVariable Long id) {
        return saleCartService.getEntity(id);
    }

    //    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    @PostMapping
    public ResponseEntity<SaleCartDto> createEntity(@Valid @RequestBody SaleCartCreateRequest request, UriComponentsBuilder uriBuilder) {
        var categoryDto = saleCartService.createEntity(request);
        var uri = uriBuilder.path("/sale-carts/{id}").buildAndExpand(categoryDto.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryDto);
    }
    @PostMapping("/{id}/add-sale-cart-item")
    public SaleCartItemDto addSaleCartItem(@PathVariable(name = "id") Long id, @RequestBody SaleCartItemAddRequest request) {
        return saleCartService.addSaleCartItem(id, request);
    }
    @PostMapping("/{saleCartId}/update-sale-cart-item/{saleCartItemId}")
    public SaleCartItemDto updateSaleCartItem(@PathVariable(name = "saleCartId") Long saleCartId,@PathVariable(name = "saleCartItemId") Long saleCartItemId, @RequestBody UpdateSaleCartItemRequest request) {
        return saleCartService.updateSaleCartItem(saleCartId,saleCartItemId, request);
    }
    //    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    @PutMapping("/{id}")
    public SaleCartDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateSaleCartRequest request) {
        return saleCartService.updateEntity(id, request);
    }

    //    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    @DeleteMapping("/{id}")
    public SaleCartDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return saleCartService.deleteEntity(id);
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