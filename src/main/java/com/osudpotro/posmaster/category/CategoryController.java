package com.osudpotro.posmaster.category;

import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.picture.PictureDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class



CategoryController {
    private final CategoryService categoryService;

    //    @PreAuthorize("hasAuthority('CATEGORY_READ')")
    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.gerAllCategories();
    }

    @PostMapping("/filter")
    public PagedResponse<CategoryDto> searchCategories(
            @RequestBody CategoryFilter filter,
//            @RequestParam Map<String, String> params,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CategoryDto> result = categoryService.getCategories(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_cat_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return categoryService.importCategories(file);
    }

    //    @PreAuthorize("hasAuthority('CATEGORY_READ')")
    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    //    @PreAuthorize("hasAuthority('CATEGORY_CREATE')")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryCreateRequest request, UriComponentsBuilder uriBuilder) {
        var categoryDto = categoryService.CreateCategory(request);
        var uri = uriBuilder.path("/categories/{id}").buildAndExpand(categoryDto.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryDto);
    }

    //    @PreAuthorize("hasAuthority('CATEGORY_UPDATE')")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(
            @PathVariable(name = "id") Long id,
            @RequestBody CategoryUpdateRequest request) {
        return categoryService.updateCategory(id, request);
    }

    //    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    @DeleteMapping("/{id}")
    public CategoryDto deleteCategory(
            @PathVariable(name = "id") Long id) {
        return categoryService.deleteCategory(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkCategory(@RequestBody CategoryBulkUpdateRequest request) {
        int count = categoryService.deleteBulkCategory(request.getCatIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    //    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    @GetMapping("/{id}/activate")
    public CategoryDto activateCategory(
            @PathVariable(name = "id") Long id) {
        return categoryService.activeCategory(id);
    }

    //    @PreAuthorize("hasAuthority('CATEGORY_DELETE')")
    @GetMapping("/{id}/deactivate")
    public CategoryDto deactivateCategory(
            @PathVariable(name = "id") Long id) {
        return categoryService.deactivateCategory(id);
    }

    @ExceptionHandler(DuplicateCategoryException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateCategory(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }

    @ExceptionHandler(CategoryImageException.class)
    public ResponseEntity<Map<String, String>> handleCategoryImage(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Image Id is invalid.")
        );
    }

    @ExceptionHandler(ParentCategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleParentCategoryNotFound(Exception ex) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(CategorySelfParentException.class)
    public ResponseEntity<Map<String, String>> handleSelfParentCategory(Exception ex) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Void> handleCategoryNotFound() {
        return ResponseEntity.notFound().build();
    }
}