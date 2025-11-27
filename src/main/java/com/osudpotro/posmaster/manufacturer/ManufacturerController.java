package com.osudpotro.posmaster.manufacturer;

import com.osudpotro.posmaster.common.PagedResponse;
import com.osudpotro.posmaster.genericunit.GenericUnitBulkUpdateRequest;
import com.osudpotro.posmaster.genericunit.GenericUnitDto;
import com.osudpotro.posmaster.genericunit.GenericUnitFilter;
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
@RequestMapping("/manufacturers")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;
    @GetMapping
    public List<ManufacturerDto> getAllManufacturers(){
        return manufacturerService.gerAllManufacturers();
    }
    @PostMapping("/filter")
    public PagedResponse<ManufacturerDto> filterManufacturers(
            @RequestBody ManufacturerFilter filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ManufacturerDto> result = manufacturerService.getManufacturers(filter, pageable);
        return new PagedResponse<>(result);
    }

    @PostMapping("/upload_csv")
    public int uploadCsvFile(@RequestParam("filepond") MultipartFile file) {
        return manufacturerService.importManufacturer(file);
    }
    @GetMapping("/{id}")
    public ManufacturerDto getManufacturer(@PathVariable Long id) {
        return manufacturerService.getManufacturer(id);
    }
    @PostMapping
    public ResponseEntity<ManufacturerDto> createManufacturer(@Valid @RequestBody ManufacturerCreateRequest request, UriComponentsBuilder uriBuilder){
        var genericDto = manufacturerService.createManufacturer(request);
        var uri=uriBuilder.path("/manufacturers/{id}").buildAndExpand(genericDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(genericDto);
    }
    @PutMapping("/{id}")
    public ManufacturerDto updateManufacturer(
            @PathVariable(name = "id") Long id,
            @RequestBody ManufacturerUpdateRequest request) {
        return manufacturerService.updateManufacturer(id, request);
    }
    @DeleteMapping("/{id}")
    public ManufacturerDto deleteManufacturer(
            @PathVariable(name = "id") Long id) {
        return manufacturerService.deleteManufacturer(id);
    }
    @PostMapping("/delete-bulk")
    public ResponseEntity<Map<String, Integer>> deleteBulkManufacturer(@RequestBody ManufacturerBulkUpdateRequest request) {
        var count = manufacturerService.deleteBulkManufacturer(request.getManufacturerIds());
        return ResponseEntity.ok().body(
                Map.of("count", count)
        );
    }
    @GetMapping("/{id}/activate")
    public ManufacturerDto activeManufacturer(
            @PathVariable(name = "id") Long id) {
        return manufacturerService.activeManufacturer(id);
    }

    @GetMapping("/{id}/deactivate")
    public ManufacturerDto deactivateGeneric(
            @PathVariable(name = "id") Long id) {
        return manufacturerService.deactivateManufacturer(id);
    }

    @ExceptionHandler(DuplicateManufacturerException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateManufacturer(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }
    @ExceptionHandler(ManufacturerNotFoundException.class)
    public ResponseEntity<Void> handleManufacturerNotFound() {
        return ResponseEntity.notFound().build();
    }
}