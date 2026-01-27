package com.osudpotro.posmaster.requisition;

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
@RequestMapping("/requisitions")
public class RequisitionController {
    private final RequisitionService requisitionService;
    @GetMapping
    public List<RequisitionDto> getAllRequisitions(){
        return requisitionService.getAllRequisitions();
    }
    @PostMapping("/filter")
    public PagedResponse<RequisitionDto> filterRequisitions(
            @RequestBody RequisitionFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RequisitionDto> result = requisitionService.filterRequisitions(filter, pageable);
        return new PagedResponse<>(result);
    }
    @GetMapping("/{id}")
    public RequisitionDto getRequisition(@PathVariable Long id) {
        return requisitionService.getRequisition(id);
    }
}
