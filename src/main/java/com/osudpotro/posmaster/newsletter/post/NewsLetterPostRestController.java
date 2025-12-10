//package com.osudpotro.posmaster.newsletter.post;
//
//import com.osudpotro.posmaster.newsletter.post.NewsLetterPostDTO;
//import com.osudpotro.posmaster.user.User;
//import com.osudpotro.posmaster.user.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/newsletter/post")
//@RequiredArgsConstructor
//public class NewsLetterPostRestController {
//
//    private final NewsLetterPostService service;
//    private final NewsLetterPostMapper mapper;
//    private final UserRepository userRepository;
//
//    // Get all with pagination
//    @GetMapping
//    public Page<NewsLetterPostDTO> getAll(@RequestParam(defaultValue = "0") int page,
//                                          @RequestParam(defaultValue = "5") int size) {
//        return service.getAllPosts(page, size)
//                .map(mapper::toDTO);
//    }
//
//    // Get by ID
//    @GetMapping("/{id}")
//    public NewsLetterPostDTO getOne(@PathVariable Long id) {
//        return mapper.toDTO(service.getPost(id));
//    }
//
//    // Create
//    @PostMapping
//    public NewsLetterPostDTO createPost(@RequestBody NewsLetterPostCreateRequest request) {
//        return mapper.toDTO(service.createPost(request));
//    }
//
//    // Update
//    @PutMapping("/{id}")
//    public NewsLetterPostDTO updatePost(@PathVariable Long id, @RequestBody NewsLetterPostUpdateRequest request) {
//        return mapper.toDTO(service.updatePost(id, request));
//    }
//
//    // Delete
//    @DeleteMapping("/{id}")
//    public void deletePost(@PathVariable Long id) {
//        service.deletePost(id);
//    }
//}

package com.osudpotro.posmaster.newsletter.post;

import com.osudpotro.posmaster.picture.PictureNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class NewsLetterPostRestController {

    private final NewsLetterPostService service;
    private final NewsLetterPostMapper mapper;

    // Get all with pagination
    @GetMapping
    public Page<NewsLetterPostDTO> getAll(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size) {
        return service.getAllPosts(page, size).map(mapper::toDTO);
    }

    // Get by ID
    @GetMapping("/{id}")
    public NewsLetterPostDTO getOne(@PathVariable Long id) {
        return mapper.toDTO(service.getPost(id));
    }

    // Create
    @PostMapping
    public NewsLetterPostDTO createPost(@RequestBody NewsLetterPostCreateRequest request) {
        return mapper.toDTO(service.createPost(request));
    }

    // Update
    @PutMapping("/{id}")
    public NewsLetterPostDTO updatePost(@PathVariable Long id, @RequestBody NewsLetterPostUpdateRequest request) {
        return mapper.toDTO(service.updatePost(id, request));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        service.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // Global Exception Handlers
    @ExceptionHandler(NewsLetterPostNotFoundException.class)
    public ResponseEntity<String> handlePostNotFound(NewsLetterPostNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(PictureNotFoundException.class)
    public ResponseEntity<String> handlePictureNotFound(PictureNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(NewsLetterPageNotFoundException.class)
    public ResponseEntity<String> handlePageNotFound(NewsLetterPageNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherExceptions(Exception ex) {
        return ResponseEntity.status(500).body("An unexpected error occurred: " + ex.getMessage());
    }
}
