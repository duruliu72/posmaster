package com.osudpotro.posmaster.newsletter.page;

import com.osudpotro.posmaster.newsletter.page.NewsLetterPageDTO;
import com.osudpotro.posmaster.picture.Picture;
import com.osudpotro.posmaster.newsletter.NewsLetter;
import org.springframework.stereotype.Component;

@Component
public class NewsLetterPageMapper {

    // Entity -> DTO
    public NewsLetterPageDTO toDTO(NewsLetterPage page) {
        if (page == null) return null;

        NewsLetterPageDTO dto = new NewsLetterPageDTO();
        dto.setId(page.getId());
        dto.setTitle(page.getTitle());
        dto.setPageNo(page.getPageNo());
        dto.setBannerId(page.getBanner() != null ? page.getBanner().getId() : null);
        dto.setNewsLetterId(page.getNewsLetter() != null ? page.getNewsLetter().getId() : null);
        return dto;
    }

    // DTO -> Entity
    public NewsLetterPage toEntity(NewsLetterPageDTO dto, Picture banner, NewsLetter newsletter) {
        if (dto == null) return null;

        NewsLetterPage page = new NewsLetterPage();
        page.setId(dto.getId());
        page.setTitle(dto.getTitle());
        page.setPageNo(dto.getPageNo());
        page.setBanner(banner);
        page.setNewsLetter(newsletter);
        return page;
    }

    // Update existing entity with DTO data
    public void updateEntity(NewsLetterPage page, NewsLetterPageDTO dto, Picture banner, NewsLetter newsletter) {
        page.setTitle(dto.getTitle());
        page.setPageNo(dto.getPageNo());
        page.setBanner(banner);
        page.setNewsLetter(newsletter);
    }
}
