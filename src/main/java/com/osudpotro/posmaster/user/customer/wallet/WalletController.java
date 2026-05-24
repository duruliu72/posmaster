package com.osudpotro.posmaster.user.customer.wallet;


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

@AllArgsConstructor
@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletService walletService;

    @GetMapping
    public List<WalletDto> getAllEntities() {
        return walletService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<WalletDto> getAllEntities(
            @RequestBody WalletFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<WalletDto> result = walletService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @GetMapping("/{id}")
    public WalletDto getEntity(@PathVariable Long id) {
        return walletService.getEntity(id);
    }

    @PostMapping
    public ResponseEntity<WalletDto> createEntity(@Valid @RequestBody WalletCreateRequest request, UriComponentsBuilder uriBuilder) {
        var entityDto = walletService.createEntity(request);
        var uri = uriBuilder.path("/wallets/{id}").buildAndExpand(entityDto.getId()).toUri();
        return ResponseEntity.created(uri).body(entityDto);
    }

    @PutMapping("/{id}")
    public WalletDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody WalletUpdateRequest request) {
        return walletService.updateEntity(id, request);
    }
}
