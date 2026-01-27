package com.osudpotro.posmaster.multimedia;

import lombok.Data;

@Data
public class MultimediaDto {
    private Long id;
    private String name;
    private String imageUrl;
    private Integer mediaType;
    private Integer sourceLink;
}
