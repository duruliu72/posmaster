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
@RequestMapping("/inventory-summary")
public class InventorySummaryController {
    private final InventorySummaryService invSummaryService;
    @GetMapping
    public List<InventorySummaryGroupDto> getGroupInventorySummary() {
        return invSummaryService.getGroupInventorySummary();
    }
    @PostMapping("/filter-page")
    public PagedResponse<InventorySummaryGroupDto> filterEntitiesWithOnlyPage(
            @RequestBody InventorySummaryFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventorySummaryGroupDto> result = invSummaryService.filterEntitiesWithOnlyPage(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/filter")
    public PagedResponse<InventorySummaryGroupDto> filterEntities(
            @RequestBody InventorySummaryFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventorySummaryGroupDto> result = invSummaryService.filterGroupInventorySummary(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/projection-filter")
    public PagedResponse<InventorySummaryGroupProjection> filterEntitiesWithProjection(
            @RequestBody InventorySummaryFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventorySummaryGroupProjection> result = invSummaryService.filterGroupInventorySummaryProjection(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/filter-by-branch")
    public PagedResponse<InventorySummaryGroupProjection> filterGroupInventoryListByBranch(
            @RequestBody InventorySummaryFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<InventorySummaryGroupProjection> result = invSummaryService.filterGroupInventorySummaryProjection(filter, pageable);
        return new PagedResponse<>(result);
    }
}
