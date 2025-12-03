package com.osudpotro.posmaster.newsletter;

import lombok.Data;

@Data
public class NewsLetterUpdateRequest {
    private String name;
    private Boolean isPublish;
    private Long pictureId;
}
