package com.osudpotro.posmaster.dispatch;

import com.osudpotro.posmaster.common.PagedResponse;
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

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@RestController
@RequestMapping("/dispatches")
public class DispatchController {
    @Autowired
    private DispatchService dispatchService;

    @GetMapping
    public List<DispatchDto> getAllDispatches() {
        return dispatchService.getAllDispatches();
    }

    @PostMapping("/filter")
    public PagedResponse<DispatchDto> getAllDispatches(
            @RequestBody DispatchFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dispatchAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DispatchDto> result = dispatchService.getAllDispatches(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/filter-requested-by-branch")
    public PagedResponse<DispatchDto> getAllDispatchesByRequestedBranch(
            @RequestBody DispatchFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dispatchAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DispatchDto> result = dispatchService.getAllDispatchesByRequestedBranch(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/filter-request-received-by-branch")
    public PagedResponse<DispatchDto> getAllDispatchesByRequestReceivedBranch(
            @RequestBody DispatchFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dispatchAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DispatchDto> result = dispatchService.getAllDispatchesByRequestReceivedBranch(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping
    public ResponseEntity<DispatchDto> createDispatch(@Valid @RequestBody DispatchCreateRequest request, UriComponentsBuilder uriBuilder) {
        var dispatchDto = dispatchService.createDispatch(request);
        var uri = uriBuilder.path("/dispatches/{id}").buildAndExpand(dispatchDto.getId()).toUri();
        return ResponseEntity.created(uri).body(dispatchDto);
    }

    @PutMapping("/{id}/send-by-requester-branch")
    public DispatchDto sendByRequesterBranch(@PathVariable(name = "id") Long id, @Valid @RequestBody DispatchUpdateRequest request) {
        return dispatchService.sendByRequesterBranch(id, request);
    }
    @PutMapping("/{id}/accept-by-requester-branch")
    public DispatchDto acceptByRequesterBranch(@PathVariable(name = "id") Long id, @Valid @RequestBody DispatchUpdateRequest request) {
        return dispatchService.acceptByRequesterBranch(id, request);
    }
    @PutMapping("/{id}/accept-by-request-receive-branch")
    public DispatchDto acceptByAcceptorBranch(@PathVariable(name = "id") Long id, @Valid @RequestBody DispatchUpdateRequest request) {
        return dispatchService.acceptByAcceptorBranch(id, request);
    }
    @PutMapping("/{id}/send-by-request-receive-branch")
    public DispatchDto sendByAcceptorBranch(@PathVariable(name = "id") Long id, @Valid @RequestBody DispatchUpdateRequest request) {
        return dispatchService.sendByAcceptorBranch(id, request);
    }
    //  For Get  Dispatch with item Pagination
    @PostMapping("/{id}/filter")
    public DispatchWithItemPageResponse filterWithItemPagination(@PathVariable Long id,
                                                                 @RequestBody DispatchItemFilter filter,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size,
                                                                 @RequestParam(defaultValue = "id") String sortBy,
                                                                 @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return dispatchService.filterWithItemPagination(id, pageable, filter);
    }

    @PostMapping("/{dispatchId}/add-item")
    public DispatchDto addDispatchItem(@PathVariable Long dispatchId, @RequestBody DispatchItemAddRequest request) {
        return dispatchService.addDispatchItem(dispatchId, request);
    }

    @ExceptionHandler(DispatchException.class)
    public ResponseEntity<Map<String, String>> handleDispatchException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }

}
