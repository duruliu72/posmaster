package com.osudpotro.posmaster.picture;

import com.osudpotro.posmaster.category.CategoryCreateRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/pictures")
public class PictureController {
    private final PictureService pictureService;
    @PostMapping(value = "/upload444", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(
            @RequestPart("data") CategoryCreateRequest request,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.ok("Uploaded!");
    }
//    @PreAuthorize("hasAuthority('BRANCH_READ')")
    @GetMapping
    public List<PictureDto> getAllPictures(){
        return pictureService.getAllPictures();
    }
    @GetMapping("/{id}")
    public PictureDto getPicture(@PathVariable Long id) {
        return pictureService.getPicture(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<PictureDto> createPicture(@RequestParam("filepond") MultipartFile file, UriComponentsBuilder uriBuilder){
        var branchDto = pictureService.createPicture(file);
        var uri=uriBuilder.path("/branches/{id}").buildAndExpand(branchDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(branchDto);
    }
    @PostMapping(value ="/upload-file" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PictureDto> createPictureFile(@Valid @ModelAttribute PictureCreateRequest request, UriComponentsBuilder uriBuilder){
        var branchDto = pictureService.createPictureFile(request);
        var uri=uriBuilder.path("/branches/{id}").buildAndExpand(branchDto.getId()).toUri();
        return  ResponseEntity.created(uri).body(branchDto);
    }
    @PutMapping("/{id}")
    public PictureDto updatePicture(
            @PathVariable(name = "id") Long id,
            @RequestBody PictureUpdateRequest request) {
        return pictureService.updatePicture(id, request);
    }
    @DeleteMapping("/{id}")
    public PictureDto deletePicture(
            @PathVariable(name = "id") Long id) {
        return pictureService.deletePicture(id);
    }

    @GetMapping("/{id}/activate")
    public PictureDto activatePicture(
            @PathVariable(name = "id") Long id) {
        return pictureService.activatePicture(id);
    }

    @GetMapping("/{id}/deactivate")
    public PictureDto deactivatePicture(
            @PathVariable(name = "id") Long id) {
        return pictureService.deactivatePicture(id);
    }


    @ExceptionHandler(DuplicatePictureException.class)
    public ResponseEntity<Map<String, String>> handleDuplicatePicture(Exception ex) {
        return ResponseEntity.badRequest().body(
                Map.of("name", "Name is already exist.")
        );
    }
    @ExceptionHandler(DirectoryAlreadyExist.class)
    public ResponseEntity<Map<String, String>> handlePictureDirectory(Exception ex) {
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
    @ExceptionHandler(PictureNotFoundException.class)
    public ResponseEntity<Void> handlePictureNotFound() {
        return ResponseEntity.notFound().build();
    }
}
