package com.osudpotro.posmaster.inventory;

import com.osudpotro.posmaster.common.PagedResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService invSummaryService;
    @GetMapping
    public List<InventoryGroupDto> getGroupInventorySummary() {
        return invSummaryService.getGroupInventorySummary();
    }
    @PostMapping("/filter-page")
    public PagedResponse<InventoryGroupDto> filterEntitiesWithOnlyPage(
            @RequestBody InventoryFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryGroupDto> result = invSummaryService.filterEntitiesWithOnlyPage(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/filter")
    public PagedResponse<InventoryGroupDto> filterEntities(
            @RequestBody InventoryFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryGroupDto> result = invSummaryService.filterGroupInventorySummary(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/projection-filter")
    public PagedResponse<InventoryByGroupProjection> filterEntitiesWithProjection(
            @RequestBody InventoryFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryByGroupProjection> result = invSummaryService.filterGroupInventorySummaryProjection(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/filter-by-branch")
    public PagedResponse<InventoryByGroupProjection> filterGroupInvSummaryByBranch(
            @RequestBody InventoryFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventoryByGroupProjection> result = invSummaryService.filterGroupInvSummaryByBranch(filter, pageable);
        return new PagedResponse<>(result);
    }
}
