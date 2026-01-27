package com.osudpotro.posmaster.category;

import lombok.Data;

@Data
public class CategoryCreateRequest {
    private String name;
    private String prefix;
    private String description;
    private Long parent_cat_id;
    private Long pictureId;
    private Long multimediaId;
}