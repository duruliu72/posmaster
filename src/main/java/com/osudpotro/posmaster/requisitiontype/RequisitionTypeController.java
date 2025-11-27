package com.osudpotro.posmaster.requisitiontype;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/requisition-types")
public class RequisitionTypeController {
    private final RequisitionTypeService requsitionTypeService;
    @GetMapping
    public List<RequisitionTypeDto> getAllRequisitionTypes(){
        return requsitionTypeService.gerAllRequisitionTypes();
    }
    @GetMapping("/{id}")
    public RequisitionTypeDto getRequisitionType(@PathVariable Long id) {
        return requsitionTypeService.getRequisitionType(id);
    }
    @PostMapping
    public ResponseEntity<RequisitionTypeDto> createRequisitionType(@Valid @RequestBody RequisitionTypeCreateRequest request, UriComponentsBuilder uriBuilder){
        var requsitionTypeDto = requsitionTypeService.createRequisitionType(request);
        var uri=uriBuilder.path("/requsitionTypes/{id}").buildAndExpand(requsitionTypeDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(requsitionTypeDto);
    }
    @PutMapping("/{id}")
    public RequisitionTypeDto updateRequisitionType(
            @PathVariable(name = "id") Long id,
            @RequestBody RequisitionTypeUpdateRequest request) {
        return requsitionTypeService.updateRequisitionType(id, request);
    }
    @DeleteMapping("/{id}")
    public RequisitionTypeDto deleteRequisitionType(
            @PathVariable(name = "id") Long id) {
        return requsitionTypeService.deleteRequisitionType(id);
    }

    @GetMapping("/{id}/activate")
    public RequisitionTypeDto activateRequisitionType(
            @PathVariable(name = "id") Long id) {
        return requsitionTypeService.activeRequisitionType(id);
    }

    @GetMapping("/{id}/deactivate")
    public RequisitionTypeDto deactivateRequisitionType(
            @PathVariable(name = "id") Long id) {
        return requsitionTypeService.deactivateRequisitionType(id);
    }

    @ExceptionHandler(DuplicateRequisitionTypeException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateRequisitionType(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }
    @ExceptionHandler(RequisitionTypeNotFoundException.class)
    public ResponseEntity<Void> handleRequisitionTypeNotFound() {
        return ResponseEntity.notFound().build();
    }
}