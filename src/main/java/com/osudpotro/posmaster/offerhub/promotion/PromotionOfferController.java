package com.osudpotro.posmaster.offerhub.promotion;

import com.osudpotro.posmaster.common.DuplicateEntityException;
import com.osudpotro.posmaster.common.EntityException;
import com.osudpotro.posmaster.common.EntityNotFoundException;
import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.offerhub.offer.*;
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
@RequestMapping("/promotion-offers")
public class PromotionOfferController {
    private final PromotionOfferService promotionOfferService;

    //    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<PromotionOfferDto> getAllEntities() {
        return promotionOfferService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<PromotionOfferDto> getAllEntities(
            @RequestBody PromotionOfferFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PromotionOfferDto> result = promotionOfferService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @GetMapping("/{id}")
    public PromotionOfferDto getEntity(@PathVariable Long id) {
        return promotionOfferService.getEntity(id);
    }

    @PostMapping
    public ResponseEntity<PromotionOfferDto> createEntity(@Valid @RequestBody PromotionOfferCreateRequest request, UriComponentsBuilder uriBuilder) {
        var entityDto = promotionOfferService.createEntity(request);
        var uri = uriBuilder.path("/offers/{id}").buildAndExpand(entityDto.getId()).toUri();
        return ResponseEntity.created(uri).body(entityDto);
    }

    @PutMapping("/{id}")
    public PromotionOfferDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody PromotionOfferUpdateRequest request) {
        return promotionOfferService.updateEntity(id, request);
    }

    @DeleteMapping("/{id}")
    public PromotionOfferDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return promotionOfferService.deleteEntity(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkEntity(@RequestBody OfferBulkUpdateRequest request) {
        var count = promotionOfferService.deleteBulkEntity(request.getOfferIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public PromotionOfferDto activateEntity(
            @PathVariable(name = "id") Long id) {
        return promotionOfferService.activateEntity(id);
    }

    @GetMapping("/{id}/deactivate")
    public PromotionOfferDto deactivateEntity(
            @PathVariable(name = "id") Long id) {
        return promotionOfferService.deactivateEntity(id);
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