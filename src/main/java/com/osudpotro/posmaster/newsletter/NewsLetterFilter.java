package com.osudpotro.posmaster.newsletter;

import lombok.Data;

@Data
public class NewsLetterFilter {
    private String name;
    private Boolean isPublish;
    private Integer status;
}
