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
    @PostMapping
    public ResponseEntity<DispatchDto> createDispatch(@Valid @RequestBody DispatchCreateRequest request, UriComponentsBuilder uriBuilder) {
        var dispatchDto = dispatchService.createDispatch(request);
        var uri = uriBuilder.path("/dispatches/{id}").buildAndExpand(dispatchDto.getId()).toUri();
        return ResponseEntity.created(uri).body(dispatchDto);
    }
    @ExceptionHandler(DispatchException.class)
    public ResponseEntity<Map<String, String>> handleDispatchException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }

}
