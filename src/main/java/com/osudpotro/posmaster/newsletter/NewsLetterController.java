package com.osudpotro.posmaster.newsletter;

import com.osudpotro.posmaster.common.PagedResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
@AllArgsConstructor
@RestController
@RequestMapping("/newsletters")
public class NewsLetterController {
    private final NewsLetterService newsLetterService;

    //    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping
    public List<NewsLetterDto> getAllNewsLetters() {
        return newsLetterService.gerAllNewsLetters();
    }

    @PostMapping("/filter")
    public PagedResponse<NewsLetterDto> searchNewsLetters(
            @RequestBody NewsLetterFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<NewsLetterDto> result = newsLetterService.getNewsLetters(filter, pageable);
        return new PagedResponse<>(result);
    }
    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return newsLetterService.importNewsLetters(file);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    @GetMapping("/{id}")
    public NewsLetterDto getNewsLetter(@PathVariable Long id) {
        return newsLetterService.getNewsLetter(id);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_CREATE')")
    @PostMapping
    public ResponseEntity<NewsLetterDto> createNewsLetter(@Valid @RequestBody NewsLetterCreateRequest request, UriComponentsBuilder uriBuilder) {
        var customerDto = newsLetterService.createNewsLetter(request);
        var uri = uriBuilder.path("/ui-resources/{id}").buildAndExpand(customerDto.getId()).toUri();
        return ResponseEntity.created(uri).body(customerDto);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_UPDATE')")
    @PutMapping("/{id}")
    public NewsLetterDto updateNewsLetter(
            @PathVariable(name = "id") Long id,
            @RequestBody NewsLetterUpdateRequest request) {
        return newsLetterService.updateNewsLetter(id, request);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    @DeleteMapping("/{id}")
    public NewsLetterDto deleteNewsLetter(
            @PathVariable(name = "id") Long id) {
        return newsLetterService.deleteNewsLetter(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkNewsLetter(@RequestBody NewsLetterBulkUpdateRequest request) {
        int count = newsLetterService.deleteBulkNewsLetter(request.getNewsletterIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }
    //    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    @GetMapping("/{id}/activate")
    public NewsLetterDto activateNewsLetter(
            @PathVariable(name = "id") Long id) {
        return newsLetterService.activeNewsLetter(id);
    }

    //    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    @GetMapping("/{id}/deactivate")
    public NewsLetterDto deactivateNewsLetter(
            @PathVariable(name = "id") Long id) {
        return newsLetterService.deactivateNewsLetter(id);
    }
    @ExceptionHandler(DuplicateNewsLetterException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateNewsLetter(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("error", ex.getMessage())
        );
    }
    @ExceptionHandler(NewsLetterNotFoundException.class)
    public ResponseEntity<Void> handleNewsLetterNotFound() {
        return ResponseEntity.notFound().build();
    }
}
