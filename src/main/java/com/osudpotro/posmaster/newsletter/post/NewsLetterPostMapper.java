package com.osudpotro.posmaster.newsletter.post;

import com.osudpotro.posmaster.newsletter.post.NewsLetterPostDTO;
import com.osudpotro.posmaster.picture.Picture;
import org.springframework.stereotype.Component;

@Component
public class NewsLetterPostMapper {

    public NewsLetterPostDTO toDTO(NewsLetterPost post) {
        NewsLetterPostDTO dto = new NewsLetterPostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setSortDesc(post.getSortDesc());
        dto.setLongDesc(post.getLongDesc());
        dto.setLink(post.getLink());
        Picture picture = new Picture();
        picture.setId(post.getImage().getId());
        picture.setName(post.getImage().getName());
        picture.setImageUrl(post.getImage().getImageUrl());
        dto.setImage(picture);
        dto.setPageId(post.getNewsLetterPage() != null ? post.getNewsLetterPage().getId() : null);
        return dto;
    }
}
