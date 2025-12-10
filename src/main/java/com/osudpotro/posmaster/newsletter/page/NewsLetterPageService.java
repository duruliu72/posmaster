package com.osudpotro.posmaster.newsletter.page;

import com.osudpotro.posmaster.newsletter.NewsLetterRepository;
import com.osudpotro.posmaster.newsletter.page.NewsLetterPageDTO;
import com.osudpotro.posmaster.newsletter.NewsLetter;
import com.osudpotro.posmaster.picture.Picture;
import com.osudpotro.posmaster.picture.PictureRepository;
import com.osudpotro.posmaster.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class NewsLetterPageService {

    @Autowired
    private NewsLetterPageRepository pageRepository;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private NewsLetterRepository newsLetterRepository;

    @Autowired
    private NewsLetterPageMapper mapper;

    // Pagination
    public Page<NewsLetterPage> getAllPages(int page, int size) {
        return pageRepository.findAll(PageRequest.of(page, size));
    }

    // Get by ID
    public NewsLetterPage getPage(Long id){
        return pageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Page not found"));
    }

    // Create
    public NewsLetterPage createPage(NewsLetterPageDTO dto, User user){
        Picture banner = null;
        NewsLetter newsletter = null;

        if (dto.getBannerId() != null) {
            banner = pictureRepository.findById(dto.getBannerId())
                    .orElseThrow(() -> new RuntimeException("Banner not found"));
        }

        if (dto.getNewsLetterId() != null) {
            newsletter = newsLetterRepository.findById(dto.getNewsLetterId())
                    .orElseThrow(() -> new RuntimeException("Newsletter not found"));
        }

        NewsLetterPage page = mapper.toEntity(dto, banner, newsletter);
        page.setCreatedBy(user);
        page.setUpdatedBy(user);

        return pageRepository.save(page);
    }

    // Update
    public NewsLetterPage updatePage(Long id, NewsLetterPageDTO dto, User user){
        NewsLetterPage page = getPage(id);

        Picture banner = null;
        NewsLetter newsletter = null;

        if (dto.getBannerId() != null) {
            banner = pictureRepository.findById(dto.getBannerId())
                    .orElseThrow(() -> new RuntimeException("Banner not found"));
        }

        if (dto.getNewsLetterId() != null) {
            newsletter = newsLetterRepository.findById(dto.getNewsLetterId())
                    .orElseThrow(() -> new RuntimeException("Newsletter not found"));
        }

        mapper.updateEntity(page, dto, banner, newsletter);
        page.setUpdatedBy(user);

        return pageRepository.save(page);
    }

    // Delete
    public void deletePage(Long id){
        pageRepository.deleteById(id);
    }
}
