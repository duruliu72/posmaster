package com.osudpotro.posmaster.address.area;

import org.springframework.stereotype.Component;

@Component
public class AreaMapper {
    //Mapping Here
    //Entity → DTO
    public AreaDto toDto(Area area) {
        AreaDto areaDto = new AreaDto();
        areaDto.setId(area.getId());
        areaDto.setName(area.getName());
        areaDto.setIsSubArea(area.getIsSubArea());
        String fullPath=getFullPath(area);
        areaDto.setFullPath(fullPath);
        if(area.getParentArea()!=null){
            Area parentArea=area.getParentArea();
            String parentPath=getFullPath(parentArea);
            areaDto.setParentPath(parentPath);

            AreaDto parentAreaDto = new AreaDto();
            parentAreaDto.setId(parentArea.getId());
            parentAreaDto.setName(parentArea.getName());
            parentAreaDto.setIsSubArea(parentArea.getIsSubArea());
            String chileParentPath=getFullPath(parentArea);
            parentAreaDto.setFullPath(chileParentPath);
            areaDto.setParentArea(parentAreaDto);
        }
        return areaDto;
    }
    private String getFullPath(Area area){
        String fullPath="";
        if(area.getParentArea()!=null){
            Area pArea=area.getParentArea();
            String catName=area.getName();
            fullPath=getFullPath(pArea)+">"+catName+fullPath;
        }else {
            fullPath=area.getName();
        }
        return fullPath;
    }
}
