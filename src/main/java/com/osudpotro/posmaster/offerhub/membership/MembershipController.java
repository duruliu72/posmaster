package com.osudpotro.posmaster.offerhub.membership;

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
@RequestMapping("/memberships")
public class MembershipController {
    private final MembershipService membershipService;

    //    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<MembershipDto> getAllEntities() {
        return membershipService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<MembershipDto> getAllEntities(
            @RequestBody MembershipFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MembershipDto> result = membershipService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }

    @GetMapping("/{id}")
    public MembershipDto getEntity(@PathVariable Long id) {
        return membershipService.getEntity(id);
    }

    @PostMapping
    public ResponseEntity<MembershipDto> createEntity(@Valid @RequestBody MembershipCreateRequest request, UriComponentsBuilder uriBuilder) {
        var entityDto = membershipService.createEntity(request);
        var uri = uriBuilder.path("/branches/{id}").buildAndExpand(entityDto.getId()).toUri();
        return ResponseEntity.created(uri).body(entityDto);
    }

    @PutMapping("/{id}")
    public MembershipDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody MembershipUpdateRequest request) {
        return membershipService.updateEntity(id, request);
    }

    @DeleteMapping("/{id}")
    public MembershipDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return membershipService.deleteEntity(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkEntity(@RequestBody MembershipBulkUpdateRequest request) {
        var count = membershipService.deleteBulkEntity(request.getMembershipIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public MembershipDto activateEntity(
            @PathVariable(name = "id") Long id) {
        return membershipService.activateEntity(id);
    }

    @GetMapping("/{id}/deactivate")
    public MembershipDto deactivateEntity(
            @PathVariable(name = "id") Long id) {
        return membershipService.deactivateEntity(id);
    }


    @ExceptionHandler(DuplicateMembershipException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateMembershipException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }
    @ExceptionHandler(MembershipException.class)
    public ResponseEntity<Map<String, String>> handleMembershipException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }
    @ExceptionHandler(MembershipNotFoundException.class)
    public ResponseEntity<Void> handleMembershipNotFound() {
        return ResponseEntity.notFound().build();
    }

}