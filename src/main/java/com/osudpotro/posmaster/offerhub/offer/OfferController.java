package com.osudpotro.posmaster.offerhub.offer;

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
@RequestMapping("/offers")
public class OfferController {
    private final OfferService offerService;

    //    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<OfferDto> getAllEntities() {
        return offerService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<OfferDto> getAllEntities(
            @RequestBody OfferFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<OfferDto> result = offerService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @GetMapping("/{id}")
    public OfferDto getEntity(@PathVariable Long id) {
        return offerService.getEntity(id);
    }

    @PostMapping
    public ResponseEntity<OfferDto> createEntity(@Valid @RequestBody OfferCreateRequest request, UriComponentsBuilder uriBuilder) {
        var entityDto = offerService.createEntity(request);
        var uri = uriBuilder.path("/offers/{id}").buildAndExpand(entityDto.getId()).toUri();
        return ResponseEntity.created(uri).body(entityDto);
    }

    @PutMapping("/{id}")
    public OfferDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody OfferUpdateRequest request) {
        return offerService.updateEntity(id, request);
    }

    @DeleteMapping("/{id}")
    public OfferDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return offerService.deleteEntity(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkEntity(@RequestBody OfferBulkUpdateRequest request) {
        var count = offerService.deleteBulkEntity(request.getOfferIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public OfferDto activateEntity(
            @PathVariable(name = "id") Long id) {
        return offerService.activateEntity(id);
    }

    @GetMapping("/{id}/deactivate")
    public OfferDto deactivateEntity(
            @PathVariable(name = "id") Long id) {
        return offerService.deactivateEntity(id);
    }


    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEntityException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
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