package com.osudpotro.posmaster.address.area;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/areas")
public class AreaController {

    @Autowired
    private AreaRepository areaRepo;

    private final AreaService areaService;

    //    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<AreaDto> getAllEntities() {
        return areaService.getAllEntities();
    }

    @PostMapping("/filter")
    public PagedResponse<AreaDto> getAllEntities(
            @RequestBody AreaFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AreaDto> result = areaService.getAllEntities(filter, pageable);
        return new PagedResponse<>(result);
    }


        @GetMapping("/active")
        public ResponseEntity<?> getActiveAreas() {
            List<Area> areas = areaRepo.findByStatusAndIsSubArea(1, false);
            List<Map<String, Object>> result = areas.stream().map(a -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", a.getId());
                map.put("name", a.getName());
                return map;
            }).toList();
            return ResponseEntity.ok(result);
        }


    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return areaService.importEntities(file);
    }

    @GetMapping("/{id}")
    public AreaDto getEntity(@PathVariable Long id) {
        return areaService.getEntity(id);
    }

    @PostMapping
    public ResponseEntity<AreaDto> createEntity(@Valid @RequestBody AreaCreateRequest request, UriComponentsBuilder uriBuilder) {
        var entityDto = areaService.createEntity(request);
        var uri = uriBuilder.path("/branches/{id}").buildAndExpand(entityDto.getId()).toUri();
        return ResponseEntity.created(uri).body(entityDto);
    }

    @PutMapping("/{id}")
    public AreaDto updateEntity(
            @PathVariable(name = "id") Long id,
            @RequestBody AreaUpdateRequest request) {
        return areaService.updateEntity(id, request);
    }

    @DeleteMapping("/{id}")
    public AreaDto deleteEntity(
            @PathVariable(name = "id") Long id) {
        return areaService.deleteEntity(id);
    }

    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkEntity(@RequestBody AreaBulkUpdateRequest request) {
        var count = areaService.deleteBulkEntity(request.getAreaIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }

    @GetMapping("/{id}/activate")
    public AreaDto activateEntity(
            @PathVariable(name = "id") Long id) {
        return areaService.activateEntity(id);
    }

    @GetMapping("/{id}/deactivate")
    public AreaDto deactivateEntity(
            @PathVariable(name = "id") Long id) {
        return areaService.deactivateEntity(id);
    }


    @ExceptionHandler(DuplicateAreaException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateAreaException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }
    @ExceptionHandler(AreaException.class)
    public ResponseEntity<Map<String, String>> handleAreaException(Exception e) {
        return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
        );
    }
    @ExceptionHandler(AreaNotFoundException.class)
    public ResponseEntity<Void> handleAreaNotFound() {
        return ResponseEntity.notFound().build();
    }

}