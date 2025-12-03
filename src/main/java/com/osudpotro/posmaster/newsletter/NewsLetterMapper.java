package com.osudpotro.posmaster.newsletter;

import org.springframework.stereotype.Component;

@Component
public class NewsLetterMapper {
    public NewsLetterDto toDto(NewsLetter newsLetter) {
        NewsLetterDto newsLetterDto = new NewsLetterDto();
        newsLetterDto.setId(newsLetter.getId());
        newsLetterDto.setName(newsLetter.getName());
        newsLetterDto.setIsPublish(newsLetter.getIsPublish());
        return newsLetterDto;
    }
    public NewsLetter toEntity(NewsLetterCreateRequest request) {
        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setName(request.getName());
        newsLetter.setIsPublish(request.getIsPublish());
        return newsLetter;
    }
    public void update(NewsLetterUpdateRequest request, NewsLetter newsLetter) {
        newsLetter.setName(request.getName());
        newsLetter.setIsPublish(request.getIsPublish());
    }
}
