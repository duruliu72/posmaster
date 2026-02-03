package com.osudpotro.posmaster.multimedia;

import com.osudpotro.posmaster.manufacturer.ManufacturerDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/multimedias")
public class MultimediaController {
    @Autowired
    private MultimediaService multimediaService;
    @PostMapping(value = "/upload444", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @RequestPart("data") MultimediaCreateRequest request,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.ok("Uploaded!");
    }
    //    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<MultimediaDto> getAllMultimedias(){
        return multimediaService.getAllMultimedias();
    }
    @PostMapping("/by_ids")
    public List<MultimediaDto> getMultimediaListByIds(@RequestBody MultimediaFilterByIds filter) {
        return multimediaService.getMultimediaListByIds(filter.getMultimediaIds());
    }
    @GetMapping("/{id}")
    public MultimediaDto getMultimedia(@PathVariable Long id) {
        return multimediaService.getMultimedia(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<MultimediaDto> createMultimedia(@RequestParam("filepond") MultipartFile file, UriComponentsBuilder uriBuilder){
        var branchDto = multimediaService.createMultimedia(file);
        var uri=uriBuilder.path("/multimedias/{id}").buildAndExpand(branchDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(branchDto);
    }
    @PostMapping(value ="/upload-file" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MultimediaDto> createMultimediaFile(@Valid @ModelAttribute MultimediaCreateRequest request, UriComponentsBuilder uriBuilder){
        var branchDto = multimediaService.createMultimediaFile(request);
        var uri=uriBuilder.path("/multimedias/{id}").buildAndExpand(branchDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(branchDto);
    }
    @PutMapping("/{id}")
    public MultimediaDto updateMultimedia(
            @PathVariable(name = "id") Long id,
            @RequestBody MultimediaUpdateRequest request) {
        return multimediaService.updateMultimedia(id, request);
    }
    @DeleteMapping("/{id}")
    public MultimediaDto deleteMultimedia(
            @PathVariable(name = "id") Long id) {
        return multimediaService.deleteMultimedia(id);
    }

    @GetMapping("/{id}/activate")
    public MultimediaDto activateMultimedia(
            @PathVariable(name = "id") Long id) {
        return multimediaService.activateMultimedia(id);
    }

    @GetMapping("/{id}/deactivate")
    public MultimediaDto deactivateMultimedia(
            @PathVariable(name = "id") Long id) {
        return multimediaService.deactivateMultimedia(id);
    }


    @ExceptionHandler(DuplicateMultimediaException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateMultimedia(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }
    @ExceptionHandler(DirectoryAlreadyExist.class)
    public ResponseEntity<Map<String, String>> handleMultimediaDirectory(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Directory is already exist.")
        );
    }
    @ExceptionHandler(CopyFileException.class)
    public ResponseEntity<Map<String, String>> handleCopyFile(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Copy File issue is already exist.")
        );
    }
    @ExceptionHandler(FileRequiredException.class)
    public ResponseEntity<Map<String, String>> handleFileRequired(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "File is required")
        );
    }
    @ExceptionHandler(MultimediaNotFoundException.class)
    public ResponseEntity<Void> handleMultimediaNotFound() {
        return ResponseEntity.notFound().build();
    }
}
