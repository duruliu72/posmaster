package com.osudpotro.posmaster.newsletter.post;

import lombok.Data;

@Data
public class NewsLetterPostUpdateRequest {
    private Long id;
    private String title;
    private String sortDesc;
    private String longDesc;
    private String link;
    private Long imageId;
    private Long pageId;
}
