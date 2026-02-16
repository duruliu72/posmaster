package com.osudpotro.posmaster.category;

import com.osudpotro.posmaster.multimedia.MultimediaDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomCategoryMapper {
    //Mapping Here
    //Entity → DTO
   public CategoryDto toDto(Category category){
        CategoryDto categoryDto=new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        if(category.getParentCat()!=null){
            ParentCategoryDto parentCatDto=new ParentCategoryDto();
            parentCatDto.setId(category.getParentCat().getId());
            parentCatDto.setName(category.getParentCat().getName());
            categoryDto.setParentCat(parentCatDto);
        }
        String fullPath=getFullString(category);
        categoryDto.setFullPath(fullPath);
       if(category.getMedia()!=null&&category.getMedia().getImageUrl()!=null){
           MultimediaDto multimediaDto=new MultimediaDto();
           multimediaDto.setId(category.getMedia().getId());
           multimediaDto.setName(category.getMedia().getName());
           multimediaDto.setImageUrl(category.getMedia().getImageUrl());
           multimediaDto.setMediaType(category.getMedia().getMediaType());
           multimediaDto.setSourceLink(category.getMedia().getSourceLink());
           categoryDto.setMedia(multimediaDto);
       }
        List<Long> catIds=new ArrayList<>();
        categoryDto.setCatIds(getCatWithParents(category,catIds));
        return  categoryDto;
    }
    private String getFullString(Category category){
        String fullPath="";
        if(category.getParentCat()!=null){
            Category pCat=category.getParentCat();
            String catName=category.getName();
            fullPath=getFullString(pCat)+">>"+catName+fullPath;
        }else {
            fullPath=category.getName();
        }
        return fullPath;
    }
    private List<Long> getCatWithParents(Category category,List<Long> catIds){
        if(category.getParentCat()!=null){
            catIds.add(category.getId());
            getCatWithParents(category.getParentCat(),catIds);
        }else {
            catIds.add(category.getId());
        }
        return catIds;
    }
}


