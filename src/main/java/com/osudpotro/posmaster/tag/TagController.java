package com.osudpotro.posmaster.tag;

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
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    @GetMapping
    public List<TagDto> getAllTags(){
        return tagService.getAllTags();
    }
    @PostMapping("/filter")
    public PagedResponse<TagDto> filterManufacturers(
            @RequestBody TagFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<TagDto> result = tagService.getTags(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return tagService.importTag(file);
    }
    @GetMapping("/{id}")
    public TagDto getTag(@PathVariable Long id) {
        return tagService.getTag(id);
    }
    @PostMapping
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody TagCreateRequest request, UriComponentsBuilder uriBuilder){
        var genericDto = tagService.createTag(request);
        var uri=uriBuilder.path("/manufacturers/{id}").buildAndExpand(genericDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(genericDto);
    }
    @PutMapping("/{id}")
    public TagDto updateTag(
            @PathVariable(name = "id") Long id,
            @RequestBody TagUpdateRequest request) {
        return tagService.updateTag(id, request);
    }
    @DeleteMapping("/{id}")
    public TagDto deleteTag(
            @PathVariable(name = "id") Long id) {
        return tagService.deleteTag(id);
    }
    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkTag(@RequestBody TagBulkUpdateRequest request) {
        var count = tagService.deleteBulkTag(request.getTagIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }
    @GetMapping("/{id}/activate")
    public TagDto activateTag(
            @PathVariable(name = "id") Long id) {
        return tagService.activeTag(id);
    }

    @GetMapping("/{id}/deactivate")
    public TagDto deactivateGeneric(
            @PathVariable(name = "id") Long id) {
        return tagService.deactivateTag(id);
    }

    @ExceptionHandler(DuplicateTagException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateTag(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }
    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<Void> handleTagNotFound() {
        return ResponseEntity.notFound().build();
    }
}
