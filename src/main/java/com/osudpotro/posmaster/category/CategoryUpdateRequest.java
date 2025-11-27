package com.osudpotro.posmaster.category;

import com.osudpotro.posmaster.picture.PictureDto;
import lombok.Data;

@Data
public class CategoryUpdateRequest {
    private String name;
    private String description;
    private Long parent_cat_id;
    private Long pictureId;
}