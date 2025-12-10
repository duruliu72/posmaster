package com.osudpotro.posmaster.newsletter.page;

import com.osudpotro.posmaster.newsletter.page.NewsLetterPageDTO;
import com.osudpotro.posmaster.user.User;
import com.osudpotro.posmaster.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/newsletter/page")
public class NewsLetterPageRestController {

    @Autowired
    private NewsLetterPageService service;

    @Autowired
    private NewsLetterPageMapper mapper;

    @Autowired
    private UserRepository userRepository;

    // GET ALL PAGES WITH PAGINATION
    @GetMapping
    public Page<NewsLetterPageDTO> getAllPages(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "5") int size) {
        return service.getAllPages(page, size)
                .map(mapper::toDTO);
    }

    // GET PAGE BY ID
    @GetMapping("/{id}")
    public NewsLetterPageDTO getPage(@PathVariable Long id) {
        return mapper.toDTO(service.getPage(id));
    }

    // CREATE
    @PostMapping
    public NewsLetterPageDTO createPage(@RequestBody NewsLetterPageDTO dto) {
        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.toDTO(service.createPage(dto, user));
    }

    // UPDATE
    @PutMapping("/{id}")
    public NewsLetterPageDTO updatePage(@PathVariable Long id, @RequestBody NewsLetterPageDTO dto) {
        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.toDTO(service.updatePage(id, dto, user));
    }

    //DELETE
    @DeleteMapping("/{id}")
    public void deletePage(@PathVariable Long id) {
        service.deletePage(id);
    }
}
