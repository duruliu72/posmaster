package com.osudpotro.posmaster.newsletter.post;

import com.osudpotro.posmaster.picture.Picture;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsLetterPostDTO {
    private Long id;
    private String title;
    private String sortDesc;
    private String longDesc;
    private String link;
    private Picture image;
    private Long pageId;
}
