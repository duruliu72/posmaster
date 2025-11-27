package com.osudpotro.posmaster.genericunit;

import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.generic.GenericBulkUpdateRequest;
import com.osudpotro.posmaster.generic.GenericDto;
import com.osudpotro.posmaster.generic.GenericFilter;
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
@RequestMapping("/generic-units")
public class GenericUnitController {
    private final GenericUnitService genericUnitService;
    @GetMapping
    public List<GenericUnitDto> getAllGenericUnits(){
        return genericUnitService.getAllGenericUnits();
    }
    @PostMapping("/filter")
    public PagedResponse<GenericUnitDto> filterGenericUnits(
            @RequestBody GenericUnitFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<GenericUnitDto> result = genericUnitService.getGenericUnits(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return genericUnitService.importGenericUnit(file);
    }
    @GetMapping("/{id}")
    public GenericUnitDto getGenericUnit(@PathVariable Long id) {
        return genericUnitService.getGenericUnit(id);
    }

    @PostMapping
    public ResponseEntity<GenericUnitDto> createGenericUnit(@Valid @RequestBody GenericUnitCreateRequest request, UriComponentsBuilder uriBuilder){
        var resourceDto = genericUnitService.createGenericUnit(request);
        var uri=uriBuilder.path("/actions/{id}").buildAndExpand(resourceDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(resourceDto);
    }
    @PutMapping("/{id}")
    public GenericUnitDto updateGenericUnit(
            @PathVariable(name = "id") Long id,
            @RequestBody GenericUnitUpdateRequest request) {
        return genericUnitService.updateGenericUnit(id, request);
    }
    @DeleteMapping("/{id}")
    public GenericUnitDto deleteResource(
            @PathVariable(name = "id") Long id) {
        return genericUnitService.deleteGenericUnit(id);
    }
    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkGenericUnit(@RequestBody GenericUnitBulkUpdateRequest request) {
        var count = genericUnitService.deleteBulkGenericUnit(request.getGenericUnitIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public GenericUnitDto activateResource(
            @PathVariable(name = "id") Long id) {
        return genericUnitService.activateGenericUnit(id);
    }

    @GetMapping("/{id}/deactivate")
    public GenericUnitDto deactivateResource(
            @PathVariable(name = "id") Long id) {
        return genericUnitService.deactivateGenericUnit(id);
    }


    @ExceptionHandler(DuplicateGenericUnitException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateGenericUnit(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }
    @ExceptionHandler(GenericUnitNotFoundException.class)
    public ResponseEntity<Void> handleGenericUnitNotFound() {
        return ResponseEntity.notFound().build();
    }
}