package com.osudpotro.posmaster.newsletter;

import com.osudpotro.posmaster.picture.Picture;
import lombok.Data;

@Data
public class NewsLetterDto {
    private Long id;
    private String name;
    private Boolean isPublish;
    private Picture banner;
}
