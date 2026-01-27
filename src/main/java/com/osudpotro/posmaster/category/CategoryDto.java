package com.osudpotro.posmaster.category;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
import com.osudpotro.posmaster.picture.PictureDto;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private ParentCategoryDto parentCat;
    private String fullPath;
    private List<Long> catIds;
    private PictureDto picture;
    private MultimediaDto media;
}