package com.osudpotro.posmaster.deliverymethod;

import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.deliverycharge.DeliveryCharge;
import com.osudpotro.posmaster.deliverycharge.DeliveryChargeRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/delivery-methods")
public class DeliveryMethodController {
    private final DeliveryMethodService dvmService;

    //    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<DeliveryMethodDto> getAllEntities() {
        return dvmService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<DeliveryMethodDto> getAllEntities(
            @RequestBody DeliveryMethodFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DeliveryMethodDto> result = dvmService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @Autowired
    private DeliveryMethodRepository deliveryMethodRepo;

    @GetMapping("/active")
    public ResponseEntity<?> getActiveDeliveryMethods() {
        List<DeliveryMethod> methods = deliveryMethodRepo.findByStatusOrderByTitleAsc(1);

        List<Map<String, Object>> result = methods.stream().map(m -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", m.getId());
            map.put("title", m.getTitle());
            map.put("message", m.getMessage());
            map.put("defaultDeliveryFee", m.getDefaultDeliveryFee());
            map.put("defaultMinSaleAmountForDeliveryFree", m.getDefaultMinSaleAmountForDeliveryFree());
            return map;
        }).toList();

        return ResponseEntity.ok(result);
    }


    @Autowired
    private DeliveryChargeRepository deliveryChargeRepo;

    @GetMapping("/calculate-fee")
    public ResponseEntity<?> calculateDeliveryFee(
            @RequestParam Long deliveryMethodId,
            @RequestParam(required = false) Long areaId) {

        DeliveryMethod method = deliveryMethodRepo.findById(deliveryMethodId).orElse(null);
        if (method == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid delivery method"));
        }

        BigDecimal fee = method.getDefaultDeliveryFee();
        BigDecimal minFreeAmount = method.getDefaultMinSaleAmountForDeliveryFree();
        String chargeType = "default";

        // Check area-specific charge
        if (areaId != null) {
            Optional<DeliveryCharge> chargeOpt = deliveryChargeRepo.findByAreaAndDeliveryMethod(areaId, deliveryMethodId);
            if (chargeOpt.isPresent()) {
                DeliveryCharge charge = chargeOpt.get();
                chargeType = "area_specific";

                if (charge.getIsFree() != null && charge.getIsFree()) {
                    fee = BigDecimal.ZERO;
                } else if (charge.getDeliveryFee() != null) {
                    fee = charge.getDeliveryFee();
                }
                if (charge.getMinSaleAmountForDeliveryFree() != null) {
                    minFreeAmount = charge.getMinSaleAmountForDeliveryFree();
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("deliveryFee", fee);
        result.put("minSaleAmountForDeliveryFree", minFreeAmount);
        result.put("chargeType", chargeType);
        result.put("deliveryMethodName", method.getTitle());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return dvmService.importEntities(file);
    }

    @GetMapping("/{id}")
    public DeliveryMethodDto getEntity(@PathVariable Long id) {
        return dvmService.getEntity(id);
    }

    @PostMapping
    public ResponseEntity<DeliveryMethodDto> createEntity(@Valid @RequestBody DeliveryMethodCreateRequest request, UriComponentsBuilder uriBuilder) {
        var entityDto = dvmService.createEntity(request);
        var uri = uriBuilder.path("/delivery-methods/{id}").buildAndExpand(entityDto.getId()).toUri();
        return ResponseEntity.created(uri).body(entityDto);
    }

    @PutMapping("/{id}")
    public DeliveryMethodDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody DeliveryMethodUpdateRequest request) {
        return dvmService.updateEntity(id, request);
    }

    @DeleteMapping("/{id}")
    public DeliveryMethodDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return dvmService.deleteEntity(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkEntity(@RequestBody DeliveryMethodBulkUpdateRequest request) {
        var count = dvmService.deleteBulkEntity(request.getDeliveryMethodIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public DeliveryMethodDto activateEntity(
            @PathVariable(name = "id") Long id) {
        return dvmService.activateEntity(id);
    }

    @GetMapping("/{id}/deactivate")
    public DeliveryMethodDto deactivateEntity(
            @PathVariable(name = "id") Long id) {
        return dvmService.deactivateEntity(id);
    }


    @ExceptionHandler(DuplicateDeliveryMethodException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateAreaException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }
    @ExceptionHandler(DeliveryMethodNotFoundException.class)
    public ResponseEntity<Void> handleAreaNotFound() {
        return ResponseEntity.notFound().build();
    }




}